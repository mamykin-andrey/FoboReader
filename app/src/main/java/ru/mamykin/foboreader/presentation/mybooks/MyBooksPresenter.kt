package ru.mamykin.foboreader.presentation.mybooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.domain.mybooks.MyBooksInteractor
import javax.inject.Inject

@InjectViewState
class MyBooksPresenter @Inject constructor(
        private val interactor: MyBooksInteractor,
        override val resourcesManager: ResourcesManager,
        override val schedulers: Schedulers
) : BasePresenter<MyBooksView>() {

    private var searchQuery: String = ""
    private var sortOrder: BookDao.SortOrder = BookDao.SortOrder.BY_NAME
    var router: MyBooksRouter? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadBooks()
    }

    fun onSortBooksClicked(sortOrder: BookDao.SortOrder) {
        this.sortOrder = sortOrder
        loadBooks()
    }

    fun onQueryTextChange(searchQuery: String) {
        this.searchQuery = searchQuery
        loadBooks()
    }

    fun onBookClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .map { it.filePath }
                .applySchedulers()
                .subscribe({ router?.openBook(it) }, { onError(R.string.error_book_open) })
                .unsubscribeOnDestroy()
    }

    fun onBookAboutClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .map { it.filePath }
                .applySchedulers()
                .subscribe({ router?.openBookDetails(it) }, { onError(R.string.error_book_open) })
                .unsubscribeOnDestroy()
    }

    fun onBookShareClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .applySchedulers()
                .subscribe({ router?.openBookShareDialog(it) }, { onError(R.string.error_book_open) })
                .unsubscribeOnDestroy()
    }

    fun onBookRemoveClicked(bookPath: String) {
        interactor.removeBook(bookPath)
                .applySchedulers()
                .subscribe({ loadBooks() }, { onError(R.string.error_book_open) })
                .unsubscribeOnDestroy()
    }

    private fun loadBooks() {
        interactor.getBooks(searchQuery, sortOrder)
                .applySchedulers()
                .subscribe({ viewState.showBooks(it) }, { onError(R.string.error_book_list_loading) })
                .unsubscribeOnDestroy()
    }
}