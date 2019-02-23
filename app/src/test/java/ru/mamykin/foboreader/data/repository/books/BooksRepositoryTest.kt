package ru.mamykin.foboreader.data.repository.books

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.domain.entity.FictionBook

class BooksRepositoryTest {

    @Mock
    lateinit var bookDao: BookDao
    @Mock
    lateinit var bookParser: BookParser
    @Mock
    lateinit var mockBook: FictionBook

    lateinit var repository: BooksRepository

    private val bookPath = "/books/1.fb"

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = BooksRepository(bookDao, bookParser)
    }

    @Test
    fun getBookInfo_returnsBookInfoFromBookDao() {
        whenever(bookDao.getBook(bookPath)).thenReturn(mockBook)

        val testSubscriber = repository.getBookInfo(bookPath).test()

        testSubscriber.assertCompleted().assertValue(mockBook)
    }

    @Test
    fun removeBook_removesBookFromBookDao() {
        whenever(bookDao.getBook(bookPath)).thenReturn(mockBook)

        val testSubscriber = repository.removeBook(bookPath).test()

        testSubscriber.assertCompleted().assertNoErrors()
        verify(bookDao).getBook(bookPath)
        verify(bookDao).delete(mockBook)
    }

    @Test
    fun getBooks_returnsBooksFromBookDao() {
        val mockBooks = listOf(mockBook, mockBook)
        val sortOrder = BookDao.SortOrder.BY_NAME
        whenever(bookDao.getBooks("", sortOrder)).thenReturn(mockBooks)

        val testSubscriber = repository.getBooks("", sortOrder).test()

        testSubscriber.assertCompleted().assertValue(mockBooks)
    }

    @Test
    fun getBook_returnsParsedBookFromBookDao_whenBookExist() {
        whenever(bookDao.getBook(bookPath)).thenReturn(mockBook)
        whenever(bookParser.parse(any(), any()))
                .thenAnswer { (it.arguments[1] as () -> Unit).invoke() }

        val testSubscriber = repository.getBook(bookPath).test()

        testSubscriber.assertCompleted().assertValue(mockBook)
        verify(bookDao).update(mockBook)
    }

    @Test
    fun getBook_returnsCreatedBook_whenBookDoesntExist() {
        whenever(bookDao.getBook(bookPath)).thenReturn(null)
        whenever(bookParser.parse(any(), any()))
                .thenAnswer { (it.arguments[1] as () -> Unit).invoke() }

        val testSubscriber = repository.getBook(bookPath).test()

        testSubscriber.assertCompleted().assertNoErrors()
        verify(bookDao).update(any())
    }

    @Test
    fun updateBook_updatesBookInBookDao() {
        val testSubscriber = repository.updateBook(mockBook).test()

        testSubscriber.assertCompleted().assertNoErrors()
        verify(bookDao).update(mockBook)
    }
}