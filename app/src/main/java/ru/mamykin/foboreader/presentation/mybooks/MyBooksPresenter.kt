package ru.mamykin.foboreader.presentation.mybooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.core.extension.applySchedulers
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.domain.mybooks.MyBooksInteractor
import ru.mamykin.foboreader.core.ui.BasePresenter
import javax.inject.Inject

@InjectViewState
class MyBooksPresenter @Inject constructor(
        private val interactor: MyBooksInteractor,
        private val router: MyBooksRouter
) : BasePresenter<MyBooksView>() {

    private var searchQuery: String = ""
    private var sortOrder: BookDao.SortOrder = BookDao.SortOrder.BY_NAME

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
                .applySchedulers()
                .subscribe(router::openBook, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onBookAboutClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .map { it.filePath }
                .applySchedulers()
                .subscribe(router::openBookDetails, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onBookShareClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .applySchedulers()
                .subscribe(router::openBookShareDialog, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onBookRemoveClicked(bookPath: String) {
        interactor.removeBook(bookPath)
                .applySchedulers()
                .subscribe(this::loadBooks, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    private fun loadBooks() {
        interactor.getBooks(searchQuery, sortOrder)
                .applySchedulers()
                .subscribe(viewState::showBooks, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }
}