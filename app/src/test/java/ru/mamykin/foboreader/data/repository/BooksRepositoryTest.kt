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
    fun getBookInfo_returnsBookInfo_fromBookDao() {
        whenever(bookDao.getBook(bookId)).thenReturn(mockBook)

        val testObserver = repository.getBookInfo(bookId).test()

        testObserver.assertCompleted().assertValue(mockBook)
    }
}