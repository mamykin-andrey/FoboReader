package ru.mamykin.foboreader.store.categories

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.StringOrResource

class StoreMainViewModelTest {

    private val getBookCategories: GetBookCategoriesUseCase = mockk()
    private val errorMessageMapper: ErrorMessageMapper = mockk()
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
        coEvery { getBookCategories.execute() } returns Result.success(booksList)

        viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
        advanceUntilIdle()

        assertEquals(
            StoreMainViewModel.State.Content(booksList.map(BookCategoryUIModel::fromDomainModel)),
            viewModel.state,
        )
    }

    @Test
    fun `don't load categories when they're loaded`() = runTest {
        coEvery { getBookCategories.execute() } returns Result.success(emptyList())

        viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
        runCurrent()

        viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
        runCurrent()

        coVerify(exactly = 1) { getBookCategories.execute() }
    }

    @Test
    fun `show error when categories loading failed`() = runTest {
        val errorMessage = "test"
        val exception = IllegalStateException(errorMessage)
        every { errorMessageMapper.getMessage(exception) } returns StringOrResource.String(errorMessage)
        coEvery { getBookCategories.execute() } returns Result.failure(exception)

        viewModel.sendIntent(StoreMainViewModel.Intent.LoadCategories)
        advanceUntilIdle()

        assertEquals(
            StoreMainViewModel.State.Error(StringOrResource.String(errorMessage)),
            viewModel.state
        )
    }
}