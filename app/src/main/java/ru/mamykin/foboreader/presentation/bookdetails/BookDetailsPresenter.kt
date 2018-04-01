package ru.mamykin.foboreader.presentation.bookdetails

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.domain.bookdetails.BookDetailsInteractor
import ru.mamykin.foboreader.presentation.global.BasePresenter
import ru.mamykin.foboreader.ui.bookdetails.BookDetailsRouter
import javax.inject.Inject

@InjectViewState
class BookDetailsPresenter @Inject constructor(
        private val interactor: BookDetailsInteractor,
        private val router: BookDetailsRouter
) : BasePresenter<BookDetailsView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadBook()
    }

    private fun loadBook() {
        interactor.loadBookInfo()
                .subscribe(this::showBookInfo)
                .unsubscribeOnDestory()
    }

    private fun showBookInfo(book: FictionBook) {
        viewState.showBookName(book.bookTitle!!)
        viewState.showBookAuthor(book.bookAuthor!!)
        viewState.showBookPath(book.fileName)
        viewState.showBookCurrentPage(book.currentPage.toString())
        viewState.showBookGenre(book.bookGenre!!)
        viewState.showBookOriginalLang(book.bookSrcLang!!)
        viewState.showBookCreatedDate(book.docDate!!)
    }

    fun onReadBookClicked() {
        router.openBook(interactor.getBookId())
    }
}