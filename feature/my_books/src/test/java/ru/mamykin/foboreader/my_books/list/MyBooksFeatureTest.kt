package ru.mamykin.foboreader.my_books.list

import com.github.terrakok.cicerone.Router
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
        TestScope(),
    )
    private val testBooksList = listOf<BookInfo>(mockk(), mockk())

    @Before
    fun setUp() {
        coEvery { getMyBooks.execute(any()) } returns testBooksList
    }

    @Test
    fun init_updateLoadingState() = runTest {
        assertEquals(MyBooksFeature.State.Loading, feature.state)
    }
}