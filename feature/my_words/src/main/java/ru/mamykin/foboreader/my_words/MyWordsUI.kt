package ru.mamykin.foboreader.my_words

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MyWordsUI(appNavController: NavController) {
    Text("Hello, world!")
    // val viewModel: StoreMainViewModel = hiltViewModel()
    // LaunchedEffect(viewModel) {
    //     viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
    // }
    // val snackbarHostState = remember { SnackbarHostState() }
    // LaunchedEffect(viewModel.effectFlow) {
    //     viewModel.effectFlow.collect {
    //         takeEffect(it, snackbarHostState, appNavController)
    //     }
    // }
    // StoreMainScreen(
    //     state = viewModel.state,
    //     onIntent = viewModel::sendIntent,
    //     snackbarHostState = snackbarHostState,
    //     appNavController = appNavController,
    // )
}
//
// private suspend fun takeEffect(
//     effect: StoreMainViewModel.Effect,
//     snackbarHostState: SnackbarHostState,
//     appNavController: NavController,
// ) {
//     when (effect) {
//         is StoreMainViewModel.Effect.ShowSnackbar -> snackbarHostState.showSnackbar(effect.message)
//         is StoreMainViewModel.Effect.OpenBooksListScreen -> {
//             appNavController.navigate(AppScreen.StoreBooks.createRoute(effect.categoryId))
//         }
//     }
// }
//
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// internal fun StoreMainScreen(
//     state: StoreMainViewModel.State,
//     onIntent: (StoreMainViewModel.Intent) -> Unit,
//     snackbarHostState: SnackbarHostState,
//     appNavController: NavController,
// ) {
//     Scaffold(
//         snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
//         topBar = {
//             TopAppBar(
//                 title = {
//                     Text(text = stringResource(id = R.string.books_store_title))
//                 },
//                 windowInsets = WindowInsets(0.dp),
//                 actions = {
//                     IconButton(onClick = {
//                         appNavController.navigate(AppScreen.StoreSearch.route)
//                     }) {
//                         Icon(
//                             imageVector = Icons.Default.Search,
//                             modifier = Modifier,
//                             contentDescription = null,
//                         )
//                     }
//                 },
//             )
//         }, content = { innerPadding ->
//             Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
//                 when (state) {
//                     is StoreMainViewModel.State.Loading -> GenericLoadingIndicatorComposable()
//                     is StoreMainViewModel.State.Error -> GenericErrorStubComposable {
//                         onIntent(StoreMainViewModel.Intent.LoadCategories)
//                     }
//
//                     is StoreMainViewModel.State.Content -> ContentComposable(state, onIntent)
//                 }
//             }
//         })
// }
//
// @Composable
// private fun ContentComposable(
//     state: StoreMainViewModel.State.Content,
//     onIntent: (StoreMainViewModel.Intent) -> Unit
// ) {
//     Column {
//         state.categories.forEach { StoreCategoryItemComposable(it, onIntent) }
//     }
// }
//
// @Composable
// internal fun StoreCategoryItemComposable(
//     category: BookCategoryUIModel,
//     onIntent: (StoreMainViewModel.Intent) -> Unit,
// ) {
//     Card(
//         modifier = Modifier
//             .fillMaxWidth()
//             .padding(4.dp)
//             .clickable {
//                 onIntent(StoreMainViewModel.Intent.OpenCategory(category.id))
//             },
//     ) {
//         Row(
//             modifier = Modifier
//                 .fillMaxWidth()
//                 .padding(16.dp),
//         ) {
//             Column(
//                 modifier = Modifier.weight(1f)
//             ) {
//                 Text(
//                     text = category.name,
//                     style = TextStyles.Subtitle1,
//                     color = MaterialTheme.colorScheme.onBackground,
//                 )
//                 if (category.description != null) {
//                     Text(
//                         text = category.description,
//                         style = TextStyles.Body2,
//                         color = MaterialTheme.colorScheme.onBackground,
//                         modifier = Modifier.padding(top = 4.dp),
//                     )
//                 }
//                 Text(
//                     text = stringResource(id = R.string.books_store_category_count, category.booksCount),
//                     style = TextStyles.Body2,
//                     color = MaterialTheme.colorScheme.onBackground,
//                     modifier = Modifier.padding(top = 4.dp),
//                 )
//             }
//             Icon(
//                 imageVector = Icons.Default.ArrowRight,
//                 contentDescription = null,
//                 modifier = Modifier
//                     .align(Alignment.CenterVertically)
//                     .padding()
//             )
//         }
//     }
// }
//
// @Preview
// @Composable
// fun Preview() {
//     FoboReaderTheme {
//         StoreMainScreen(
//             state = StoreMainViewModel.State.Content(
//                 listOf(
//                     BookCategoryUIModel(
//                         "1",
//                         "Classic",
//                         "Classic books",
//                         10
//                     )
//                 )
//             ),
//             onIntent = {},
//             snackbarHostState = remember { SnackbarHostState() },
//             appNavController = rememberNavController(),
//         )
//     }
// }