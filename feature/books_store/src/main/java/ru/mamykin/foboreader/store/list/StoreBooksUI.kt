package ru.mamykin.foboreader.store.list

import android.content.Context
import android.view.View
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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import ru.mamykin.foboreader.core.extension.showSnackbarWithData
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.core.navigation.MainTabScreenRoutes
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.uikit.ErrorStubWidget
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun StoreBooksUI(
    appNavController: NavHostController,
) {
    val viewModel: StoreBooksViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(StoreBooksViewModel.Intent.LoadBooks)
    }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(
                effect = it,
                snackbarHostState = snackbarHostState,
                appNavController = appNavController,
                context = context,
            )
        }
    }
    StoreBooksScreen(
        state = viewModel.state,
        onIntent = viewModel::sendIntent,
        snackbarHostState = snackbarHostState,
        appNavController = appNavController,
    )
}

private suspend fun takeEffect(
    effect: StoreBooksViewModel.Effect,
    snackbarHostState: SnackbarHostState,
    appNavController: NavHostController,
    context: Context
) {
    when (effect) {
        is StoreBooksViewModel.Effect.ShowSnackbar -> {
            snackbarHostState.showSnackbarWithData(
                data = effect.data,
                context = context,
                duration = SnackbarDuration.Short,
                withDismissAction = true,
            )
        }

        is StoreBooksViewModel.Effect.NavigateToMyBooks -> {
            appNavController.navigate(AppScreen.Main.createRoute(MainTabScreenRoutes.MY_BOOKS)) {
                popUpTo(0)
                launchSingleTop = true
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StoreBooksScreen(
    state: StoreBooksViewModel.State,
    onIntent: (StoreBooksViewModel.Intent) -> Unit,
    snackbarHostState: SnackbarHostState,
    appNavController: NavHostController,
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
                        appNavController.popBackStack()
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
                        appNavController.navigate(AppScreen.StoreSearch.route)
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
                    is StoreBooksViewModel.State.Loading -> LoadingComposable()
                    is StoreBooksViewModel.State.Content -> ContentComposable(state, onIntent)
                    is StoreBooksViewModel.State.Error -> ErrorComposable(state, onIntent)
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
    state: StoreBooksViewModel.State.Content,
    onIntent: (StoreBooksViewModel.Intent) -> Unit
) {
    Column {
        state.books.forEach { StoreBookItemComposable(it, onIntent) }
    }
}

@Composable
internal fun StoreBookItemComposable(
    book: StoreBookUIModel,
    onIntent: (StoreBooksViewModel.Intent) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = book.cover,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop,
            )
            BookInfoComposable(book, Modifier.weight(1f))
            if (book.isOwned) {
                OpenBookButtonComposable(onIntent)
            } else {
                DownloadBookButtonComposable(onIntent, book)
            }
        }
    }
}

@Composable
private fun OpenBookButtonComposable(
    onIntent: (StoreBooksViewModel.Intent) -> Unit,
) {
    IconButton(onClick = {
        onIntent(StoreBooksViewModel.Intent.OpenMyBooks)
    }) {
        Icon(
            imageVector = Icons.Default.KeyboardDoubleArrowRight,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp),
            contentDescription = null,
        )
    }
}

@Composable
private fun DownloadBookButtonComposable(
    onIntent: (StoreBooksViewModel.Intent) -> Unit,
    book: StoreBookUIModel,
) {
    IconButton(onClick = {
        onIntent(StoreBooksViewModel.Intent.DownloadBook(book.id))
    }) {
        Icon(
            imageVector = Icons.Default.Download,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(36.dp),
            contentDescription = null,
        )
    }
}

@Composable
private fun BookInfoComposable(book: StoreBookUIModel, modifier: Modifier) {
    Column(
        modifier = modifier.then(Modifier.padding(start = 16.dp))
    ) {
        Text(
            text = book.title,
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = book.author,
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp),
        )
        Text(
            text = book.genre,
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp),
        )
        Text(
            text = book.languages.joinToString(", "),
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp),
        )
        Text(
            text = "Free",
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}

@Composable
private fun ErrorComposable(
    state: StoreBooksViewModel.State.Error,
    onIntent: (StoreBooksViewModel.Intent) -> Unit
) {
    val context = LocalContext.current
    AndroidView(factory = {
        ErrorStubWidget(it).apply {
            setMessage(state.message.toString(context))
            visibility = View.VISIBLE
            setRetryClickListener {
                onIntent(StoreBooksViewModel.Intent.LoadBooks)
            }
        }
    })
}

@Composable
@Preview
fun BooksListScreenPreview() {
    FoboReaderTheme {
        StoreBooksScreen(
            state = StoreBooksViewModel.State.Content(
                listOf(
                    StoreBookUIModel(
                        id = "1",
                        genre = "Classic",
                        author = "Pierre Cardine",
                        title = "Wonderful life",
                        languages = listOf("English, Russian"),
                        format = "fb",
                        cover = "https://m.media-amazon.com/images/I/81sG60wsNtL.jpg",
                        link = "https://www.amazon.co.uk/Wonderful-Life-Burgess-Nature-History/dp/0099273454",
                        isOwned = true,
                    )
                )
            ),
            onIntent = {},
            remember { SnackbarHostState() },
            rememberNavController(),
        )
    }
}