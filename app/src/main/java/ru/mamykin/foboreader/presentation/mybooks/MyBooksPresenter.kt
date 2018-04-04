package ru.mamykin.foboreader.presentation.mybooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.domain.mybooks.MyBooksInteractor
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.extension.applySchedulers
import ru.mamykin.foboreader.presentation.global.BasePresenter
import ru.mamykin.foboreader.ui.mybooks.MyBooksRouter
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

    fun onBookClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .map { it.filePath }
                .subscribe(router::openBook, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onBookAboutClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .map { it.filePath }
                .subscribe(router::openBookDetails, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onBookShareClicked(bookPath: String) {
        interactor.getBook(bookPath)
                .subscribe({ router.showBookShareDialog(it.bookTitle, it.docUrl!!) }, { it.printStackTrace() })
                .unsubscribeOnDestory()
    }

    fun onBookRemoveClicked(bookPath: String) {
        interactor.removeBook(bookPath)
                .subscribe(this::loadBooks, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onQueryTextChange(text: String) {
        this.searchQuery = text
        loadBooks()
    }

    private fun loadBooks() {
        interactor.getBooks(searchQuery, sortOrder)
                .applySchedulers()
                .subscribe(this::showBooks, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun showBooks(books: List<FictionBook>) {
        if (books.isEmpty()) {
            viewState.showEmptyStateView(true)
        } else {
            viewState.showBooks(books)
        }
    }
}