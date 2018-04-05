package ru.mamykin.foboreader.presentation.bookdetails

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.bookdetails.BookDetailsInteractor
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.extension.applySchedulers
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
        loadBookInfo()
    }

    private fun loadBookInfo() {
        interactor.loadBookInfo()
                .applySchedulers()
                .subscribe(this::showBookInfo, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    private fun showBookInfo(book: FictionBook) {
        viewState.showBookName(book.bookTitle)
        viewState.showBookAuthor(book.bookAuthor!!)
        viewState.showBookPath(book.filePath)
        viewState.showBookCurrentPage(book.currentPage.toString())
        viewState.showBookGenre(book.bookGenre!!)
        viewState.showBookOriginalLang(book.bookSrcLang!!)
        viewState.showBookCreatedDate(book.docDate!!)
    }

    fun onReadBookClicked() = router.openBook(interactor.getBookPath())
}