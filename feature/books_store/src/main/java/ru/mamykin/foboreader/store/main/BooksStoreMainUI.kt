package ru.mamykin.foboreader.store.main

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.getActivity
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.uikit.ErrorStubWidget
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles

// TODO: Use viewModelStore instead
private const val SCREEN_KEY = "books_categories"

private fun createAndInitViewModel(
    apiHolder: ApiHolder,
): BooksStoreMainViewModel {
    return ComponentHolder.getOrCreateComponent(key = SCREEN_KEY) {
        DaggerBooksStoreMainComponent.factory().create(
            apiHolder.commonApi(),
            apiHolder.networkApi(),
            apiHolder.navigationApi(),
            apiHolder.settingsApi(),
        )
    }.booksStoreMainViewModel().also { it.sendIntent(BooksStoreMainViewModel.Intent.LoadCategories) }
}

@Composable
fun BooksCategoriesScreen() {
    val context = LocalContext.current
    val viewModel = remember { createAndInitViewModel(context.getActivity().apiHolder()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        onDispose {
            if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                ComponentHolder.clearComponent(SCREEN_KEY)
            }
        }
    }
    BookCategoriesUI(state = viewModel.state, viewModel.effectFlow, viewModel::sendIntent)
}

private suspend fun takeEffect(
    effect: BooksStoreMainViewModel.Effect,
    snackbarHostState: SnackbarHostState,
) {
    when (effect) {
        is BooksStoreMainViewModel.Effect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
internal fun BookCategoriesUI(
    state: BooksStoreMainViewModel.State,
    effectFlow: Flow<BooksStoreMainViewModel.Effect>,
    onIntent: (BooksStoreMainViewModel.Intent) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(effectFlow) {
        effectFlow.collect {
            takeEffect(it, snackbarHostState)
        }
    }
    FoboReaderTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.books_store_title))
                }, elevation = 12.dp
            )
        }, content = {
            when (state) {
                is BooksStoreMainViewModel.State.Loading -> LoadingComposable()
                is BooksStoreMainViewModel.State.Error -> ErrorComposable(state, onIntent)
                is BooksStoreMainViewModel.State.Content -> ContentComposable(state, onIntent)
            }
        })
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
        elevation = 16.dp,
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
                    color = MaterialTheme.colors.onBackground,
                )
                if (category.description != null) {
                    Text(
                        text = category.description,
                        style = TextStyles.Body2,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Text(
                    text = stringResource(id = R.string.books_store_category_count, category.booksCount),
                    style = TextStyles.Body2,
                    color = MaterialTheme.colors.onBackground,
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
    BookCategoriesUI(
        state = BooksStoreMainViewModel.State.Content(listOf(BookCategory("1", "Classic", "Classic books", 10))),
        effectFlow = emptyFlow(),
        onIntent = {}
    )
}