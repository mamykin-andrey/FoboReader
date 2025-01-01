package ru.mamykin.foboreader.store.categories

import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
fun BooksCategoriesUI(snackbarHostState: SnackbarHostState, onBookCategoryClick: (String) -> Unit) {
    val viewModel: BooksStoreCategoriesViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(BooksStoreCategoriesViewModel.Intent.LoadCategories)
    }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, snackbarHostState, onBookCategoryClick)
        }
    }
    BookCategoriesScreen(
        state = viewModel.state,
        onIntent = viewModel::sendIntent,
    )
}

private suspend fun takeEffect(
    effect: BooksStoreCategoriesViewModel.Effect,
    snackbarHostState: SnackbarHostState,
    onBookCategoryClick: (String) -> Unit,
) {
    when (effect) {
        is BooksStoreCategoriesViewModel.Effect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
        is BooksStoreCategoriesViewModel.Effect.OpenBooksListScreen -> onBookCategoryClick(effect.categoryId)
    }
}

@Composable
private fun BookCategoriesScreen(
    state: BooksStoreCategoriesViewModel.State,
    onIntent: (BooksStoreCategoriesViewModel.Intent) -> Unit,
) {
    when (state) {
        is BooksStoreCategoriesViewModel.State.Loading -> LoadingComposable()
        is BooksStoreCategoriesViewModel.State.Error -> ErrorComposable(state, onIntent)
        is BooksStoreCategoriesViewModel.State.Content -> ContentComposable(state, onIntent)
    }
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
    state: BooksStoreCategoriesViewModel.State.Content,
    onIntent: (BooksStoreCategoriesViewModel.Intent) -> Unit
) {
    Column {
        state.categories.forEach { CategoryRowComposable(it, onIntent) }
    }
}

@Composable
private fun CategoryRowComposable(category: BookCategory, onIntent: (BooksStoreCategoriesViewModel.Intent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable {
                onIntent(BooksStoreCategoriesViewModel.Intent.OpenCategory(category.id))
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
    state: BooksStoreCategoriesViewModel.State.Error,
    onIntent: (BooksStoreCategoriesViewModel.Intent) -> Unit
) {
    val context = LocalContext.current
    AndroidView(factory = {
        ErrorStubWidget(it).apply {
            setMessage(state.message.toString(context))
            visibility = View.VISIBLE
            setRetryClickListener {
                onIntent(BooksStoreCategoriesViewModel.Intent.LoadCategories)
            }
        }
    })
}

@Preview
@Composable
fun Preview() {
    FoboReaderTheme {
        BookCategoriesScreen(
            state = BooksStoreCategoriesViewModel.State.Content(
                listOf(
                    BookCategory(
                        "1",
                        "Classic",
                        "Classic books",
                        10
                    )
                )
            ),
            onIntent = {}
        )
    }
}