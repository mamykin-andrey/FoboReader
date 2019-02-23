package ru.mamykin.foboreader.presentation.bookdetails

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.applySchedulers
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.domain.bookdetails.BookDetailsInteractor
import ru.mamykin.foboreader.domain.entity.FictionBook
import javax.inject.Inject

@InjectViewState
class BookDetailsPresenter @Inject constructor(
        private val interactor: BookDetailsInteractor,
        private val resourcesManager: ResourcesManager,
        private val schedulers: Schedulers
) : BasePresenter<BookDetailsView>() {

    var router: BookDetailsRouter? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadBookInfo()
    }

    private fun loadBookInfo() {
        interactor.getBookInfo()
                .applySchedulers(schedulers.io(), schedulers.main())
                .subscribe(
                        { showBookInfo(it) },
                        { viewState.onError(resourcesManager.getString(R.string.book_details_load_info_error)) }
                )
                .unsubscribeOnDestroy()
    }

    private fun showBookInfo(book: FictionBook) = with(viewState) {
        showBookName(book.bookTitle)
        showBookAuthor(book.bookAuthor!!)
        showBookPath(book.filePath)
        showBookCurrentPage(book.currentPage.toString())
        showBookGenre(book.bookGenre!!)
        showBookOriginalLang(book.bookSrcLang!!)
        showBookCreatedDate(book.docDate!!)
    }

    fun onReadBookClicked() = router?.openBook(interactor.getBookPath())
}