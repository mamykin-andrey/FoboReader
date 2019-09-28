package ru.mamykin.foboreader.presentation.mybooks

import androidx.lifecycle.MutableLiveData
import ru.mamykin.foboreader.core.extension.applySchedulers
import ru.mamykin.foboreader.core.extension.toLiveData
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.domain.entity.FictionBook
import ru.mamykin.foboreader.domain.mybooks.MyBooksInteractor
import javax.inject.Inject

class MyBooksViewModel @Inject constructor(
        private val interactor: MyBooksInteractor,
        private val resourcesManager: ResourcesManager,
        override val schedulers: Schedulers
) : BaseViewModel() {

    private var searchQuery: String = ""
    private var sortOrder: BookDao.SortOrder = BookDao.SortOrder.BY_NAME
    var router: MyBooksRouter? = null
    private val _books = MutableLiveData<List<FictionBook>>()
    val books get() = _books.toLiveData()

    init {
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
                .subscribe(
                        { router?.openBook(it) },
                        { /*onError(R.string.error_book_open)*/ }
                )
                .unsubscribeOnDestroy()
    }

    fun onBookAboutClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .map { it.filePath }
                .applySchedulers()
                .subscribe(
                        { router?.openBookDetails(it) },
                        { /*onError(R.string.error_book_open)*/ }
                )
                .unsubscribeOnDestroy()
    }

    fun onBookShareClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .applySchedulers()
                .subscribe(
                        { router?.openBookShareDialog(it) },
                        { /*onError(R.string.error_book_open)*/ }
                )
                .unsubscribeOnDestroy()
    }

    fun onBookRemoveClicked(bookPath: String) {
        interactor.removeBook(bookPath)
                .applySchedulers()
                .subscribe(
                        { loadBooks() },
                        { /*onError(R.string.error_book_open)*/ }
                )
                .unsubscribeOnDestroy()
    }

    private fun loadBooks() {
        interactor.getBooks(searchQuery, sortOrder)
                .applySchedulers()
                .subscribe(
                        { _books.value = it },
                        { /*onError(R.string.error_book_list_loading)*/ }
                )
                .unsubscribeOnDestroy()
    }
}