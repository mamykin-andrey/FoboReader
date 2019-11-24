package ru.mamykin.my_books.presentation

import kotlinx.coroutines.launch
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.data.model.FictionBook
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.my_books.R
import ru.mamykin.my_books.domain.MyBooksInteractor
import javax.inject.Inject

class MyBooksViewModel @Inject constructor(
        private val interactor: MyBooksInteractor
) : BaseViewModel<MyBooksViewModel.ViewState, MyBooksViewModel.Action, MyBooksRouter>(
        ViewState(isLoading = true)
) {
    private var searchQuery: String = ""
    private var sortOrder: BookDao.SortOrder = BookDao.SortOrder.BY_NAME

    init {
        loadBooks()
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.Error -> state.copy(isLoading = false, error = action.error)
    }

    private fun loadBooks() = launch {
        runCatching { interactor.getBooks(searchQuery, sortOrder) }
                .onSuccess { onAction(Action.BooksLoaded(it)) }
                .onFailure { onAction(Action.Error(R.string.error_book_list_loading)) }
    }

    private fun onSortBooksClicked(sortOrder: BookDao.SortOrder) {
        this.sortOrder = sortOrder
        loadBooks()
    }

    private fun onQueryTextChanged(searchQuery: String) {
        this.searchQuery = searchQuery
        loadBooks()
    }

    private fun onBookClicked(bookPath: String) = launch {
        runCatching { interactor.getBookFilePath(bookPath) }
                .onSuccess { router?.openBook(it) }
                .onFailure { onAction(Action.Error(R.string.error_book_open)) }
    }

    private fun onBookAboutClicked(bookPath: String) = launch {
        runCatching { interactor.getBookFilePath(bookPath) }
                .onSuccess { router?.openBookDetails(it) }
                .onFailure { onAction(Action.Error(R.string.error_book_open)) }
    }

    private fun onBookShareClicked(bookPath: String) = launch {
        runCatching { interactor.getBook(bookPath) }
                .onSuccess { router?.openBookShareDialog(it) }
                .onFailure { onAction(Action.Error(R.string.error_book_open)) }
    }

    private fun onBookRemoveClicked(bookPath: String) = launch {
        runCatching { interactor.removeBook(bookPath) }
                .onSuccess { loadBooks() }
                .onFailure { onAction(Action.Error(R.string.error_book_open)) }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnSortBooksClicked -> onSortBooksClicked(event.sortOrder)
            is Event.OnQueryTextChanged -> onQueryTextChanged(event.searchQuery)
            is Event.OnBookClicked -> onBookClicked(event.bookPath)
            is Event.OnBookAboutClicked -> onBookAboutClicked(event.bookPath)
            is Event.OnBookShareClicked -> onBookShareClicked(event.bookPath)
            is Event.OnBookRemoveClicked -> onBookRemoveClicked(event.bookPath)
        }
    }

    sealed class Event {
        data class OnSortBooksClicked(val sortOrder: BookDao.SortOrder) : Event()
        data class OnQueryTextChanged(val searchQuery: String) : Event()
        data class OnBookClicked(val bookPath: String) : Event()
        data class OnBookAboutClicked(val bookPath: String) : Event()
        data class OnBookShareClicked(val bookPath: String) : Event()
        data class OnBookRemoveClicked(val bookPath: String) : Event()
    }

    sealed class Action {
        data class BooksLoaded(val books: List<FictionBook>) : Action()
        data class Error(val error: Int) : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val books: List<FictionBook> = emptyList(),
            val error: Int? = null
    )
}