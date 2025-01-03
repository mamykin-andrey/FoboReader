package ru.mamykin.foboreader.store.list

import android.content.Context
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.mamykin.foboreader.core.extension.showSnackbarWithData
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.uikit.ErrorStubWidget
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun BooksStoreListUI(onBackClick: () -> Unit, onShowMyBooksClick: () -> Unit) {
    val viewModel: BooksStoreListViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(BooksStoreListViewModel.Intent.LoadBooks)
    }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(
                effect = it,
                snackbarHostState = snackbarHostState,
                onShowMyBooksClick = onShowMyBooksClick,
                context = context,
            )
        }
    }
    BooksListScreen(
        viewModel.state,
        viewModel::sendIntent,
        snackbarHostState,
        onBackClick,
    )
}

private suspend fun takeEffect(
    effect: BooksStoreListViewModel.Effect,
    snackbarHostState: SnackbarHostState,
    onShowMyBooksClick: () -> Unit,
    context: Context
) {
    when (effect) {
        is BooksStoreListViewModel.Effect.ShowSnackbar -> {
            snackbarHostState.showSnackbarWithData(
                data = effect.data,
                context = context,
                duration = SnackbarDuration.Short,
                withDismissAction = true,
            )
        }

        is BooksStoreListViewModel.Effect.NavigateToMyBooks -> {
            onShowMyBooksClick()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BooksListScreen(
    state: BooksStoreListViewModel.State,
    onIntent: (BooksStoreListViewModel.Intent) -> Unit,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.books_store_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            modifier = Modifier,
                            contentDescription = null,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        TODO("Not implemented")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            modifier = Modifier,
                            contentDescription = null,
                        )
                    }
                }
            )
        }, content = { innerPadding ->
            Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                when (state) {
                    is BooksStoreListViewModel.State.Loading -> LoadingComposable()
                    is BooksStoreListViewModel.State.Content -> ContentComposable(state, onIntent)
                    is BooksStoreListViewModel.State.Error -> ErrorComposable(state, onIntent)
                }
            }
        })
}

@Composable
private fun LoadingComposable() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        CircularProgressIndicator(modifier = Modifier.size(48.dp))
    }
}

@Composable
private fun ContentComposable(
    state: BooksStoreListViewModel.State.Content,
    onIntent: (BooksStoreListViewModel.Intent) -> Unit
) {
    Column {
        state.books.forEach { BookRowComposable(it, onIntent) }
    }
}

@Composable
private fun BookRowComposable(book: StoreBook, onIntent: (BooksStoreListViewModel.Intent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                onIntent(BooksStoreListViewModel.Intent.DownloadBook(book))
            },
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            AsyncImage(
                model = book.cover,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop,
            )
            Column(
                modifier = Modifier.padding(start = 16.dp)
            ) {
                Text(
                    text = book.title,
                    style = TextStyles.Subtitle1,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = book.author,
                    style = TextStyles.Subtitle1,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    text = book.genre,
                    style = TextStyles.Subtitle1,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun ErrorComposable(
    state: BooksStoreListViewModel.State.Error,
    onIntent: (BooksStoreListViewModel.Intent) -> Unit
) {
    val context = LocalContext.current
    AndroidView(factory = {
        ErrorStubWidget(it).apply {
            setMessage(state.message.toString(context))
            visibility = View.VISIBLE
            setRetryClickListener {
                onIntent(BooksStoreListViewModel.Intent.LoadBooks)
            }
        }
    })
}

@Composable
@Preview
fun BooksListScreenPreview() {
    FoboReaderTheme {
        BooksListScreen(
            state = BooksStoreListViewModel.State.Content(
                listOf(
                    StoreBook(
                        id = "1",
                        genre = "Classic",
                        author = "Pierre Cardine",
                        title = "Wonderful life",
                        lang = "English",
                        format = "fb",
                        cover = "https://m.media-amazon.com/images/I/81sG60wsNtL.jpg",
                        link = "https://www.amazon.co.uk/Wonderful-Life-Burgess-Nature-History/dp/0099273454",
                    )
                )
            ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
        )
    }
}