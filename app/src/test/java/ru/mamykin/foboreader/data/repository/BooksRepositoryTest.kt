package ru.mamykin.foboreader.data.repository

import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.entity.FictionBook

class BooksRepositoryTest {

    @Mock
    lateinit var bookDao: BookDao
    @Mock
    lateinit var mockBook: FictionBook

    val bookId = 1

    lateinit var repository: BooksRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = BooksRepository(bookDao)
    }

    @Test
    fun getBookInfo_returnsBookInfoFromBookDao() {
        whenever(bookDao.getBook(bookId)).thenReturn(mockBook)

        val testSubscriber = repository.getBookInfo(bookId).test()

        testSubscriber.assertCompleted().assertValue(mockBook)
    }

    @Test
    fun removeBook_removeBookFromBookDao() {
        whenever(bookDao.getBook(bookId)).thenReturn(mockBook)

        val testSubscriber = repository.removeBook(bookId).test()

        testSubscriber.assertCompleted().assertNoErrors()
    }

    @Test
    fun getBooks_returnsBooksFromBookDao() {
        val mockBooks = listOf(mockBook, mockBook)
        whenever(bookDao.getBooks("", BookDao.SortOrder.BY_NAME)).thenReturn(mockBooks)

        val testSubscriber = repository.getBooks("", BookDao.SortOrder.BY_NAME).test()

        testSubscriber.assertCompleted().assertValue(mockBooks)
    }

    @Test
    fun getBook_returnsBookFromBookDao_whenPassBookId() {
        whenever(bookDao.getBook(bookId)).thenReturn(mockBook)

        val testSubscriber = repository.getBook(bookId).test()

        testSubscriber.assertCompleted().assertValue(mockBook)
    }

    @Test
    fun getBook_returnsBookFromBookDao_whenPassBookPath() {
        val bookPath = "/storage/1.fb"
        whenever(bookDao.getBook(bookPath)).thenReturn(mockBook)

        val testSubscriber = repository.getBook(bookPath).test()

        testSubscriber.assertCompleted().assertValue(mockBook)
    }
}