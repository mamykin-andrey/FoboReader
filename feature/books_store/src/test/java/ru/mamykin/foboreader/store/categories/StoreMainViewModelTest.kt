package ru.mamykin.foboreader.store.categories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.StringOrResource

class StoreMainViewModelTest {

    private val getBookCategories: GetBookCategoriesUseCase = mock()
    private val errorMessageMapper: ErrorMessageMapper = mock()
    private val viewModel = StoreMainViewModel(getBookCategories, errorMessageMapper)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @Test
    fun `initial state is loading`() = runTest {
        val state = viewModel.state

        assertEquals(StoreMainViewModel.State.Loading, state)
    }

    @Test
    fun `load categories when they're not loaded`() = runTest {
        val booksList = listOf(BookCategoryEntity("1", "name", "", 5))
        whenever(getBookCategories.execute()).thenReturn(Result.success(booksList))

        viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
        advanceUntilIdle()

        assertEquals(
            StoreMainViewModel.State.Content(booksList.map(BookCategoryUIModel::fromDomainModel)),
            viewModel.state,
        )
    }

    @Test
    fun `don't load categories when they're loaded`() = runTest {
        whenever(getBookCategories.execute()).thenReturn(Result.success(emptyList()))

        viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
        runCurrent()

        viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
        runCurrent()

        verify(getBookCategories, times(1)).execute()
    }

    @Test
    fun `show error when categories loading failed`() = runTest {
        val errorMessage = "test"
        val exception = IllegalStateException(errorMessage)
        whenever(errorMessageMapper.getMessage(exception)).thenReturn(StringOrResource.String(errorMessage))
        whenever(getBookCategories.execute()).thenReturn(Result.failure(exception))

        viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
        advanceUntilIdle()

        assertEquals(
            StoreMainViewModel.State.Error(StringOrResource.String(errorMessage)),
            viewModel.state
        )
    }

    @Test
    fun `send open books list effect`() = runTest {
        val categoryId = "100"
        val effects = mutableListOf<StoreMainViewModel.Effect>()
        val effectsJob = launch { viewModel.effectFlow.toList(effects) }

        viewModel.sendIntent(StoreMainViewModel.Intent.OpenCategory(categoryId))
        runCurrent()
        effectsJob.cancel()

        assertEquals(
            StoreMainViewModel.Effect.OpenBooksListScreen(categoryId),
            effects.last(),
        )
    }
}