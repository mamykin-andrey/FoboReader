package ru.mamykin.foboreader.my_books.list

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.SnackbarData
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.my_books.sort.SortAndFilterBooks

class MyBooksViewModelTest {

    private val getMyBooks: GetMyBooksUseCase = mockk()
    private val sortAndFilterBooks: SortAndFilterBooks = mockk()
    private val removeBook: RemoveBookUseCase = mockk()
    private val errorMessageMapper: ErrorMessageMapper = mockk()
    private val myBookUIModelMapper = BookInfoUIModelMapper()
    private val viewModel = MyBooksViewModel(
        getMyBooksUseCase = getMyBooks,
        sortAndFilterBooks = sortAndFilterBooks,
        removeBookUseCase = removeBook,
        errorMessageMapper = errorMessageMapper,
        myBookUIModelMapper = myBookUIModelMapper,
    )
    private val testBook1 = DownloadedBookEntity(
        id = 1L,
        filePath = "",
        genre = "",
        coverUrl = null,
        author = "",
        title = "",
        languages = emptyList(),
        date = null,
        currentPage = 0,
        totalPages = null,
        lastOpen = 0L,
    )
    private val testBook2 = DownloadedBookEntity(
        id = 2L,
        filePath = "",
        genre = "",
        coverUrl = null,
        author = "",
        title = "",
        languages = emptyList(),
        date = null,
        currentPage = 0,
        totalPages = null,
        lastOpen = 0L,
    )
    private val testBooksList = listOf(testBook1, testBook2)

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        coEvery { getMyBooks.execute() } returns Result.success(testBooksList)
    }

    @Test
    fun `show loading state on start`() = runTest {
        assertEquals(MyBooksViewModel.State.Loading, viewModel.state)
    }

    @Test
    fun `removing book succeeded`() = runTest {
        viewModel.sendIntent(MyBooksViewModel.Intent.LoadBooks)
        coEvery { removeBook.execute(testBook1.id) } returns Result.success(Unit)

        viewModel.sendIntent(MyBooksViewModel.Intent.RemoveBook(testBook1.id))
        advanceUntilIdle()

        val state = viewModel.state as? MyBooksViewModel.State.Content
        assertEquals(listOf(testBook2), state?.books)
    }

    @Test
    fun `removing book failed`() = runTest {
        viewModel.sendIntent(MyBooksViewModel.Intent.LoadBooks)
        val testErrorMessage = StringOrResource.String("test_error")
        coEvery { removeBook.execute(testBook1.id) } returns Result.failure(Throwable())
        every { errorMessageMapper.getMessage(any()) } returns testErrorMessage

        viewModel.sendIntent(MyBooksViewModel.Intent.RemoveBook(testBook1.id))
        advanceUntilIdle()

        assertEquals(
            MyBooksViewModel.Effect.ShowSnackbar(SnackbarData((testErrorMessage))),
            viewModel.effectFlow.first()
        )
    }
}