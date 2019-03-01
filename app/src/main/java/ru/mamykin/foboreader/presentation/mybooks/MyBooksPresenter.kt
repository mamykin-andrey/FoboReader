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
        private val resourcesManager: ResourcesManager,
        private val schedulers: Schedulers
) : BasePresenter<MyBooksView>() {

    private var searchQuery: String = ""
    private var sortOrder: BookDao.SortOrder = BookDao.SortOrder.BY_NAME
    var router: MyBooksRouter? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadBooks()
    }

    fun onSortByNameSelected() {
        sortOrder = BookDao.SortOrder.BY_NAME
        loadBooks()
    }

    fun onSortByReadedSelected() {
        sortOrder = BookDao.SortOrder.BY_READED
        loadBooks()
    }

    fun onSortByDateSelected() {
        sortOrder = BookDao.SortOrder.BY_DATE
        loadBooks()
    }

    fun onQueryTextChange(searchQuery: String) {
        this.searchQuery = searchQuery
        loadBooks()
    }

    fun onBookClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .map { it.filePath }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe({ router?.openBook(it) }, { viewState.onError(resourcesManager.getString(R.string.error_book_open)) })
                .unsubscribeOnDestroy()
    }

    fun onBookAboutClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .map { it.filePath }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe({ router?.openBookDetails(it) }, { viewState.onError(resourcesManager.getString(R.string.error_book_open)) })
                .unsubscribeOnDestroy()
    }

    fun onBookShareClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe({ router?.openBookShareDialog(it) }) { viewState.onError(resourcesManager.getString(R.string.error_book_open)) }
                .unsubscribeOnDestroy()
    }

    fun onBookRemoveClicked(bookPath: String) {
        interactor.removeBook(bookPath)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe({ loadBooks() }, { viewState.onError(resourcesManager.getString(R.string.error_book_open)) })
                .unsubscribeOnDestroy()
    }

    private fun loadBooks() {
        interactor.getBooks(searchQuery, sortOrder)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .subscribe({ viewState.showBooks(it) }) { viewState.onError(resourcesManager.getString(R.string.error_book_list_loading)) }
                .unsubscribeOnDestroy()
    }
}