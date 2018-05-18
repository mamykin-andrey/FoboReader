package ru.mamykin.foboreader.presentation.bookdetails

import com.arellomobile.mvp.InjectViewState
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import ru.mamykin.foboreader.domain.bookdetails.BookDetailsInteractor
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.presentation.TestSchedulers
import ru.mamykin.foboreader.ui.bookdetails.BookDetailsRouter
import rx.Single
import java.util.*

@InjectViewState
class BookDetailsPresenterTest {

    @Mock
    lateinit var interactor: BookDetailsInteractor
    @Mock
    lateinit var router: BookDetailsRouter
    @Mock
    lateinit var viewState: `BookDetailsView$$State`
    @Mock
    lateinit var view: BookDetailsView

    lateinit var presenter: BookDetailsPresenter

    private val book: FictionBook = createBook()

    private val testSchedulers = TestSchedulers()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = BookDetailsPresenter(interactor, router, testSchedulers)
        presenter.setViewState(viewState)
    }

    @Test
    fun onFirstViewAttach_shouldLoadBookInfo() {
        whenever(interactor.getBookInfo()).thenReturn(Single.just(book))

        presenter.attachView(view)

        verify(interactor).getBookInfo()
    }

    @Test
    fun loadBookInfo_showBookInfo() {
        whenever(interactor.getBookInfo()).thenReturn(Single.just(book))

        presenter.attachView(view)

        verify(viewState).showBookName(book.bookTitle)
        verify(viewState).showBookAuthor(book.bookAuthor!!)
        verify(viewState).showBookPath(book.filePath)
        verify(viewState).showBookCurrentPage(book.currentPage.toString())
        verify(viewState).showBookGenre(book.bookGenre!!)
        verify(viewState).showBookOriginalLang(book.bookSrcLang!!)
        verify(viewState).showBookCreatedDate(book.docDate!!)
    }

    @Test
    fun onReadBookClicked_openBook() {
        whenever(interactor.getBookPath()).thenReturn(book.filePath)

        presenter.onReadBookClicked()

        verify(router).openBook(book.filePath)
    }

    private fun createBook(): FictionBook = FictionBook().apply {
        this.bookTitle = "great book"
        this.bookAuthor = "great author"
        this.filePath = "/books/greatbook.fb"
        this.currentPage = 12
        this.bookGenre = "fantasy"
        this.bookSrcLang = "en"
        this.docDate = Date(1990, 10, 10, 10, 10)
    }
}