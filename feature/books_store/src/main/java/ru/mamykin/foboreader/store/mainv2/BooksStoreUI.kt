package ru.mamykin.foboreader.store.mainv2

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.categories.BooksCategoriesUI
import ru.mamykin.foboreader.store.list.BooksStoreListUI
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme

@Composable
fun BooksStoreUI() {
    val viewModel: BooksStoreViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    LaunchedEffect(viewModel) {
        scope.launch {
            delay(2_000)
            viewModel.sendIntent(BooksStoreViewModel.Intent.SwitchTab)
        }
    }
    BooksStoreUI(
        state = viewModel.state,
        onIntent = viewModel::sendIntent,
        snackbarHostState = snackbarHostState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BooksStoreUI(
    state: BooksStoreViewModel.State,
    onIntent: (BooksStoreViewModel.Intent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val navController = rememberNavController()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(top = 0.dp),
                title = {
                    // Text(text = stringResource(id = R.string.books_store_title))
                    // IconButton(onClick = {
                    //                         onBackClick()
                    //                     }) {
                    //                         Icon(
                    //                             imageVector = Icons.Default.ArrowBack,
                    //                             modifier = Modifier,
                    //                             contentDescription = null,
                    //                         )
                    //                     }
                    // actions = {
                    //                     IconButton(onClick = {
                    //                         TODO("Not implemented")
                    //                     }) {
                    //                         Icon(
                    //                             imageVector = Icons.Default.Search,
                    //                             modifier = Modifier,
                    //                             contentDescription = null,
                    //                         )
                    //                     }
                    //                 }
                    Text(text = stringResource(id = R.string.books_store_title))
                }
            )
        }, content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = (state as BooksStoreViewModel.State.Content).currentRoute,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
            ) {
                composable(BooksStoreScreen.BooksCategoriesList.route) {
                    BooksCategoriesUI(snackbarHostState) {}
                }
                composable(BooksStoreScreen.BooksStoreList.route) {
                    BooksStoreListUI(snackbarHostState) {}
                }
            }
        })
}

@Preview
@Composable
fun Preview() {
    FoboReaderTheme {
        BooksStoreUI(
            state = BooksStoreViewModel.State.Content(BooksStoreScreen.BooksCategoriesList.route),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}