package ru.mamykin.foboreader.presentation.mybooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.model.FictionBook
import ru.mamykin.foboreader.domain.mybooks.MyBooksInteractor
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

    override fun attachView(view: MyBooksView) {
        super.attachView(view)

        loadBooksList()
    }

    fun onSortByNameSelected() {
        sortOrder = BookDao.SortOrder.BY_NAME
        loadBooksList()
    }

    fun onSortByReadedSelected() {
        sortOrder = BookDao.SortOrder.BY_READED
        loadBooksList()
    }

    fun onSortByDateSelected() {
        sortOrder = BookDao.SortOrder.BY_DATE
        loadBooksList()
    }

    fun onBookClicked(bookId: Int) {
        interactor.getBook(bookId)
                .map { it.id }
                .subscribe(router::openBook, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onBookAboutClicked(bookId: Int) {
        interactor.getBook(bookId)
                .map { it.id }
                .subscribe(router::openBookDetails, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onBookShareClicked(bookId: Int) {
        interactor.getBook(bookId)
                .subscribe({ router.showBookShareDialog(it.bookTitle!!, it.docUrl!!) }, { it.printStackTrace() })
                .unsubscribeOnDestory()
    }

    fun onBookRemoveClicked(bookId: Int) {
        interactor.removeBook(bookId)
                .subscribe(Throwable::printStackTrace, this::loadBooksList)
                .unsubscribeOnDestory()
    }

    fun onQueryTextChange(text: String) {
        this.searchQuery = text
        loadBooksList()
    }

    private fun loadBooksList() {
        interactor.getBooks(searchQuery, sortOrder)
                .subscribe(this::showBooks, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun showBooks(books: List<FictionBook>) {
        if (books.isEmpty()) {
            viewState.showEmptyStateView(true)
        } else {
            viewState.showBooksList(books)
        }
    }
}