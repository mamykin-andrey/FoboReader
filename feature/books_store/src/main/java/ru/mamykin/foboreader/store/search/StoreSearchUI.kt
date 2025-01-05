package ru.mamykin.foboreader.store.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.store.categories.BookCategory
import ru.mamykin.foboreader.store.categories.StoreCategoryItemComposable
import ru.mamykin.foboreader.store.list.StoreBook
import ru.mamykin.foboreader.store.list.StoreBookItemComposable
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun StoreSearchUI(appNavController: NavHostController) {
    val viewModel: StoreSearchViewModel = hiltViewModel()
    // LaunchedEffect(viewModel) {
    //     viewModel.sendIntent(StoreSearchViewModel.Intent.LoadBooks)
    // }
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
            Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                when (state.searchState) {
                    is StoreSearchViewModel.SearchState.NotStarted -> TypingNudgeComposable()
                    is StoreSearchViewModel.SearchState.Loading -> LoadingComposable()
                    is StoreSearchViewModel.SearchState.Failed -> LoadingFailedComposable()
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
    val closeSearch: () -> Unit = { appNavController.popBackStack() }
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { newQuery ->
            onIntent(StoreSearchViewModel.Intent.Search(newQuery))
        },
        placeholder = { Text(text = "Search") },
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
            .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
            .focusRequester(focusRequester)
    )
    BackHandler(onBack = closeSearch)
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun TypingNudgeComposable() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text("Type something to search")
    }
}

@Composable
private fun LoadingFailedComposable() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        // TODO: Replace with the ErrorStub widget
        Text("The network request has failed, please try again later")
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
    // TODO: Check and fix performance
    Column {
        if (state.categories.isNotEmpty()) {
            SectionTitleComposable("Categories")
            state.categories.forEach {
                StoreCategoryItemComposable(it) {
                    // TODO:
                }
            }
        }
        if (state.books.isNotEmpty()) {
            SectionTitleComposable("Books")
            state.books.forEach {
                StoreBookItemComposable(it) {
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
            // text = stringResource(id = R.string.my_books_no_books),
            text = "Nothing found, try to change your query",
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
                        BookCategory(
                            "0",
                            "Classic",
                            "Classic books for all time",
                            2
                        ),
                        BookCategory(
                            "1",
                            "Thriller",
                            "Suspense books",
                            1
                        ),
                    ),
                    books = listOf(
                        StoreBook(
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