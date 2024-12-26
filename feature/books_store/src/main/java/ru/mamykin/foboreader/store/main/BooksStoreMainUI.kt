package ru.mamykin.foboreader.store.main

import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.uikit.ErrorStubWidget
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun BooksCategoriesScreen(onBookCategoryClick: (String) -> Unit) {
    val viewModel: BooksStoreMainViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(BooksStoreMainViewModel.Intent.LoadCategories)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, snackbarHostState, onBookCategoryClick)
        }
    }
    BookCategoriesUI(
        viewModel.state,
        viewModel::sendIntent,
        snackbarHostState,
    )
}

private suspend fun takeEffect(
    effect: BooksStoreMainViewModel.Effect,
    snackbarHostState: SnackbarHostState,
    onBookCategoryClick: (String) -> Unit,
) {
    when (effect) {
        is BooksStoreMainViewModel.Effect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
        is BooksStoreMainViewModel.Effect.OpenBooksListScreen -> onBookCategoryClick(effect.categoryId)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BookCategoriesUI(
    state: BooksStoreMainViewModel.State,
    onIntent: (BooksStoreMainViewModel.Intent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.books_store_title))
                },
                windowInsets = WindowInsets(0.dp),
            )
        }, content = { innerPadding ->
            Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                when (state) {
                    is BooksStoreMainViewModel.State.Loading -> LoadingComposable()
                    is BooksStoreMainViewModel.State.Error -> ErrorComposable(state, onIntent)
                    is BooksStoreMainViewModel.State.Content -> ContentComposable(state, onIntent)
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
    state: BooksStoreMainViewModel.State.Content,
    onIntent: (BooksStoreMainViewModel.Intent) -> Unit
) {
    Column {
        state.categories.forEach { CategoryRowComposable(it, onIntent) }
    }
}

@Composable
private fun CategoryRowComposable(category: BookCategory, onIntent: (BooksStoreMainViewModel.Intent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                onIntent(BooksStoreMainViewModel.Intent.OpenCategory(category.id))
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.name,
                    style = TextStyles.Subtitle1,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                if (category.description != null) {
                    Text(
                        text = category.description,
                        style = TextStyles.Body2,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Text(
                    text = stringResource(id = R.string.books_store_category_count, category.booksCount),
                    style = TextStyles.Body2,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding()
            )
        }
    }
}

@Composable
private fun ErrorComposable(
    state: BooksStoreMainViewModel.State.Error,
    onIntent: (BooksStoreMainViewModel.Intent) -> Unit
) {
    AndroidView(factory = {
        ErrorStubWidget(it).apply {
            setMessage(state.errorMessage)
            visibility = View.VISIBLE
            setRetryClickListener {
                onIntent(BooksStoreMainViewModel.Intent.LoadCategories)
            }
        }
    })
}

@Preview
@Composable
fun Preview() {
    FoboReaderTheme {
        BookCategoriesUI(
            state = BooksStoreMainViewModel.State.Content(listOf(BookCategory("1", "Classic", "Classic books", 10))),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}