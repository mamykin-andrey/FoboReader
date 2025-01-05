package ru.mamykin.foboreader.store.search

import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.categories.BookCategoryUIModel
import ru.mamykin.foboreader.store.categories.StoreCategoryItemComposable
import ru.mamykin.foboreader.store.list.StoreBookItemComposable
import ru.mamykin.foboreader.store.list.StoreBookUIModel
import ru.mamykin.foboreader.uikit.ErrorStubWidget
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun StoreSearchUI(appNavController: NavHostController) {
    val viewModel: StoreSearchViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    // LaunchedEffect(viewModel.effectFlow) {
    //     viewModel.effectFlow.collect {
    //         takeEffect(
    //             effect = it,
    //             snackbarHostState = snackbarHostState,
    //             context = context,
    //             appNavController = appNavController,
    //         )
    //     }
    // }
    MyBooksScreenUI(
        state = viewModel.state,
        onIntent = viewModel::sendIntent,
        snackbarHostState = snackbarHostState,
        appNavController = appNavController,
    )
}

// private suspend fun takeEffect(
//     effect: StoreSearchViewModel.Effect,
//     snackbarHostState: SnackbarHostState,
//     context: Context,
//     appNavController: NavHostController,
// ) {
//     when (effect) {
//         is StoreSearchViewModel.Effect.ShowSnackbar -> {
//             snackbarHostState.showSnackbarWithData(effect.data, context)
//         }
//
//         is StoreSearchViewModel.Effect.NavigateToBookDetails -> {
//             appNavController.navigate(AppScreen.BookDetails.createRoute(effect.bookId))
//         }
//
//         is StoreSearchViewModel.Effect.NavigateToReadBook -> {
//             appNavController.navigate(AppScreen.ReadBook.createRoute(effect.bookId))
//         }
//     }
// }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyBooksScreenUI(
    state: StoreSearchViewModel.State,
    onIntent: (StoreSearchViewModel.Intent) -> Unit,
    snackbarHostState: SnackbarHostState,
    appNavController: NavHostController,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            val searchQuery = state.searchQuery
            TopAppBar(
                title = {},
                actions = {
                    SearchFieldComposable(searchQuery, onIntent, appNavController)
                })
        }, content = { innerPadding ->
            Box(
                modifier = Modifier
                    .imePadding()
                    .padding(top = innerPadding.calculateTopPadding(), bottom = innerPadding.calculateBottomPadding())
            ) {
                when (state.searchState) {
                    is StoreSearchViewModel.SearchState.NotStarted -> TypeNudgeComposable()
                    is StoreSearchViewModel.SearchState.Loading -> LoadingComposable()
                    is StoreSearchViewModel.SearchState.Failed -> LoadingFailedComposable(state.searchState, onIntent)
                    is StoreSearchViewModel.SearchState.Loaded -> ContentComposable(state.searchState, onIntent)
                }
            }
        })
}

@Composable
private fun SearchFieldComposable(
    searchQuery: String,
    onIntent: (StoreSearchViewModel.Intent) -> Unit,
    appNavController: NavHostController,
) {
    val closeSearch = remember(appNavController) { { appNavController.popBackStack(); Unit } }
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = remember(onIntent) {
            { newQuery ->
                onIntent(StoreSearchViewModel.Intent.Search(newQuery))
            }
        },
        placeholder = { Text(text = stringResource(R.string.books_store_menu_search)) },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = closeSearch) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .focusRequester(focusRequester)
    )
    BackHandler(onBack = closeSearch)
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun TypeNudgeComposable() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(stringResource(R.string.bs_search_type_nudge))
    }
}

@Composable
private fun LoadingFailedComposable(
    searchState: StoreSearchViewModel.SearchState.Failed,
    onIntent: (StoreSearchViewModel.Intent) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current
        AndroidView(factory = {
            ErrorStubWidget(it).apply {
                setMessage(searchState.errorMessage.toString(context))
                visibility = View.VISIBLE
                setRetryClickListener {
                    onIntent(StoreSearchViewModel.Intent.RetrySearch)
                }
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
    state: StoreSearchViewModel.SearchState.Loaded,
    onIntent: (StoreSearchViewModel.Intent) -> Unit,
) {
    if (state.categories.isNotEmpty() || state.books.isNotEmpty()) {
        CategoriesAndBooksComposable(state)
    } else {
        NoSearchResultsComposable()
    }
}

@Composable
private fun CategoriesAndBooksComposable(state: StoreSearchViewModel.SearchState.Loaded) {
    LazyColumn {
        if (state.categories.isNotEmpty()) {
            items(1) {
                SectionTitleComposable(stringResource(R.string.bs_search_categories_section_title))
            }
            items(state.categories.size) {
                StoreCategoryItemComposable(state.categories[it]) {
                    // TODO:
                }
            }
        }
        if (state.books.isNotEmpty()) {
            items(1) {
                SectionTitleComposable(stringResource(R.string.bs_search_books_section_title))
            }
            items(state.books.size) {
                StoreBookItemComposable(state.books[it]) {
                    // TODO:
                }
            }
        }
    }
}

@Composable
private fun SectionTitleComposable(title: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        color = MaterialTheme.colorScheme.primary,
        text = title,
        fontSize = 18.sp
    )
}


@Composable
private fun NoSearchResultsComposable() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.bs_search_no_results),
            style = TextStyles.Subtitle1,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun MyBooksScreenPreview() {
    FoboReaderTheme {
        MyBooksScreenUI(
            state = StoreSearchViewModel.State(
                searchQuery = "",
                searchState = StoreSearchViewModel.SearchState.Loaded(
                    categories = listOf(
                        BookCategoryUIModel(
                            "0",
                            "Classic",
                            "Classic books for all time",
                            2
                        ),
                        BookCategoryUIModel(
                            "1",
                            "Thriller",
                            "Suspense books",
                            1
                        ),
                    ),
                    books = listOf(
                        StoreBookUIModel(
                            "0",
                            "Classic",
                            "Author",
                            "Title",
                            listOf("English", "Russian", "Spanish"),
                            "FBWT",
                            "",
                            "",
                        )
                    )
                )
            ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
            appNavController = rememberNavController(),
        )
    }
}