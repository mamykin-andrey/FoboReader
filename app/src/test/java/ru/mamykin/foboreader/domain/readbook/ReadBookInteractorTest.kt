package ru.mamykin.foboreader.domain.readbook

import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.data.repository.translate.TranslateRepository
import ru.mamykin.foboreader.entity.FictionBook
import rx.Single

class ReadBookInteractorTest {

    @Mock
    lateinit var booksRepository: BooksRepository
    @Mock
    lateinit var translateRepository: TranslateRepository
    @Mock
    lateinit var textToSpeechService: TextToSpeechService

    lateinit var interactor: ReadBookInteractor

    private val bookPath: String = "/books/1.fb"
    private val book = FictionBook()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        interactor = ReadBookInteractor(
                booksRepository, translateRepository, textToSpeechService, bookPath)
    }

    @Test
    fun getBookInfo_returnsBookInfo_fromRepository() {
        whenever(booksRepository.getBookInfo(bookPath)).thenReturn(Single.just(book))

        val testSubscriber = interactor.getBookInfo().test()

        testSubscriber.assertCompleted().assertValue(book)
    }
}