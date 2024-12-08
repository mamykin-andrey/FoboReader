package ru.mamykin.foboreader.my_books.list

import com.github.terrakok.cicerone.Router
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.my_books.search.FilterMyBooks
import ru.mamykin.foboreader.my_books.sort.SortMyBooks

class MyBooksFeatureTest {

    private val getMyBooks: GetMyBooks = mockk()
    private val sortMyBooks: SortMyBooks = mockk()
    private val filterMyBooks: FilterMyBooks = mockk()
    private val removeBook: RemoveBook = mockk()
    private val errorMessageMapper: ErrorMessageMapper = mockk()
    private val router: Router = mockk()
    private val screenProvider: ScreenProvider = mockk()
    private val testScope = TestScope(StandardTestDispatcher())
    private val feature = MyBooksFeature(
        MyBooksFeature.MyBooksActor(
            getMyBooks = getMyBooks,
            sortMyBooks = sortMyBooks,
            filterMyBooks = filterMyBooks,
            removeBook = removeBook,
            errorMessageMapper = errorMessageMapper,
            router = router,
            screenProvider = screenProvider,
        ),
        MyBooksFeature.MyBooksReducer(),
        testScope,
    )
    private val testBooksList = listOf<BookInfo>(mockk(), mockk())
    private val testBookId = 100L

    @Before
    fun setUp() {
        coEvery { getMyBooks.execute(any()) } returns testBooksList
    }

    @Test
    fun `show loading state on start`() = runTest {
        assertEquals(MyBooksFeature.State.Loading, feature.state)
    }

    @Test
    fun `removing book succeeded`() = runTest {
        coEvery { removeBook.execute(testBookId) } returns Result.success(Unit)

        feature.sendIntent(MyBooksFeature.Intent.RemoveBook(testBookId))
        testScope.advanceUntilIdle()

        coVerify(exactly = 2) { getMyBooks.execute(true) }
        val state = feature.state as? MyBooksFeature.State.Content
        assertTrue(state?.books == testBooksList)
    }

    @Test
    fun `removing book failed`() = runTest {
        val testErrorMessage = "test_error"
        coEvery { removeBook.execute(testBookId) } returns Result.failure(Throwable())
        every { errorMessageMapper.getMessage(any()) } returns testErrorMessage

        feature.sendIntent(MyBooksFeature.Intent.RemoveBook(testBookId))
        testScope.advanceUntilIdle()

        coVerify(exactly = 1) { getMyBooks.execute(true) }
        assertEquals(MyBooksFeature.Effect.ShowSnackbar(testErrorMessage), feature.effectFlow.first())
    }
}