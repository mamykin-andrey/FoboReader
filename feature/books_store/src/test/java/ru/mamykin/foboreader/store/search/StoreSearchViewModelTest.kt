// package ru.mamykin.foboreader.store.search
//
// import io.mockk.coEvery
// import io.mockk.mockk
// import junit.framework.TestCase.assertEquals
// import junit.framework.TestCase.assertTrue
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.test.StandardTestDispatcher
// import kotlinx.coroutines.test.advanceUntilIdle
// import kotlinx.coroutines.test.runTest
// import kotlinx.coroutines.test.setMain
// import org.junit.Before
// import org.junit.Test
// import ru.mamykin.foboreader.core.presentation.StringOrResource
//
// class StoreSearchViewModelTest {
//
//     private val searchInStoreUseCase: SearchInStoreUseCase = mockk()
//     private val viewModel = StoreSearchViewModel(searchInStoreUseCase)
//     private val searchSuccessfulResult = SearchResultsEntity(
//         emptyList(),
//         emptyList(),
//     )
//
//     @Before
//     fun setUp() {
//         Dispatchers.setMain(StandardTestDispatcher())
//     }
//
//     @Test
//     fun `initial state is search not started`() {
//         val state = viewModel.state
//
//         assertTrue(state.searchQuery.isEmpty())
//         assertEquals(StoreSearchViewModel.SearchState.NotStarted, state.searchState)
//     }
//
//     @Test
//     fun `search shows results when succeeds`() = runTest {
//         val searchQuery = "a"
//         coEvery { searchInStoreUseCase.execute(searchQuery) } returns Result.success(searchSuccessfulResult)
//
//         viewModel.sendIntent(StoreSearchViewModel.Intent.Search(searchQuery))
//         advanceUntilIdle()
//
//         val state = viewModel.state
//         assertEquals(searchQuery, state.searchQuery)
//         assertEquals(
//             StoreSearchViewModel.SearchState.Loaded(
//                 searchSuccessfulResult.categories,
//                 searchSuccessfulResult.books,
//             ), state.searchState
//         )
//     }
//
//     @Test
//     fun `search shows error when fails`() = runTest {
//         val errorMessage = "test"
//         val searchQuery = "a"
//         coEvery { searchInStoreUseCase.execute(any()) } returns Result.failure(RuntimeException(errorMessage))
//
//         viewModel.sendIntent(StoreSearchViewModel.Intent.Search(searchQuery))
//         advanceUntilIdle()
//
//         val state = viewModel.state
//         assertEquals(searchQuery, state.searchQuery)
//         assertEquals(
//             StoreSearchViewModel.SearchState.Failed(StringOrResource.String(errorMessage)), state.searchState
//         )
//     }
//
//     @Test
//     fun `search cancels the previous active search`() = runTest {
//         // val searchQuery = "a"
//         // coEvery { searchInStoreUseCase.execute(any()) } returns Result.success(searchSuccessfulResult)
//         //
//         // viewModel.sendIntent(StoreSearchViewModel.Intent.Search(searchQuery))
//         // viewModel.sendIntent(StoreSearchViewModel.Intent.Search(searchQuery))
//         // advanceUntilIdle()
//         //
//         // val state = viewModel.state
//         // assertEquals(searchQuery, state.searchQuery)
//         // assertEquals(
//         //     StoreSearchViewModel.SearchState.Failed(StringOrResource.String(errorMessage)), state.searchState
//         // )
//     }
// }