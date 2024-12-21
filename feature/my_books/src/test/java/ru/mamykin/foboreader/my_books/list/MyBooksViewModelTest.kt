package ru.mamykin.foboreader.my_books.list

import com.github.terrakok.cicerone.Router
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
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.my_books.sort.SortAndFilterBooks

class MyBooksViewModelTest {

    private val getMyBooks: LoadMyBooks = mockk()
    private val sortAndFilterBooks: SortAndFilterBooks = mockk()
    private val removeBook: RemoveBook = mockk()
    private val errorMessageMapper: ErrorMessageMapper = mockk()
    private val router: Router = mockk()
    private val screenProvider: ScreenProvider = mockk()
    private val viewModel = MyBooksViewModel(
        loadMyBooks = getMyBooks,
        sortAndFilterBooks = sortAndFilterBooks,
        removeBook = removeBook,
        errorMessageMapper = errorMessageMapper,
        router = router,
        screenProvider = screenProvider,
    )
    private val testBook1 = BookInfo(
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
    private val testBook2 = BookInfo(
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
        coEvery { getMyBooks.execute() } returns testBooksList
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
        val testErrorMessage = "test_error"
        coEvery { removeBook.execute(testBook1.id) } returns Result.failure(Throwable())
        every { errorMessageMapper.getMessage(any()) } returns testErrorMessage

        viewModel.sendIntent(MyBooksViewModel.Intent.RemoveBook(testBook1.id))
        advanceUntilIdle()

        assertEquals(MyBooksViewModel.Effect.ShowSnackbar(testErrorMessage), viewModel.effectFlow.first())
    }
}