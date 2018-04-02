package ru.mamykin.foboreader.data.repository

import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.mamykin.foboreader.data.network.BooksStoreService
import ru.mamykin.foboreader.data.repository.booksstore.BooksStoreRepository
import ru.mamykin.foboreader.entity.StoreBook
import rx.Single

class BooksStoreRepositoryTest {

    @Mock
    lateinit var booksService: BooksStoreService
    @Mock
    lateinit var mockBook: StoreBook

    lateinit var mockBooks: List<StoreBook>

    lateinit var repository: BooksStoreRepository

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        mockBooks = listOf(mockBook, mockBook)
        repository = BooksStoreRepository(booksService)
    }

    @Test
    fun getBooks_returnsBooksFromBooksService() {
        whenever(booksService.getBooks()).thenReturn(Single.just(mockBooks))

        val testSubscriber = repository.getBooks().test()

        testSubscriber.assertCompleted().assertValue(mockBooks)
    }
}