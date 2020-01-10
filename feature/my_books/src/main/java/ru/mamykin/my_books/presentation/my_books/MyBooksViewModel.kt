package ru.mamykin.my_books.presentation.my_books

import kotlinx.coroutines.launch
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.my_books.R
import ru.mamykin.my_books.domain.model.BookInfo
import ru.mamykin.my_books.domain.my_books.BooksListInteractor
import ru.mamykin.my_books.domain.my_books.SortOrder

class MyBooksViewModel constructor(
        private val interactor: BooksListInteractor
) : BaseViewModel<MyBooksViewModel.ViewState, MyBooksViewModel.Action, MyBooksRouter>(
        ViewState(isLoading = true)
) {
    private var searchQuery: String = ""
    private var sortOrder: SortOrder = SortOrder.ByName

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

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnSortBooksClicked -> onSortBooksClicked(event.order)
            is Event.OnQueryTextChanged -> onQueryTextChanged(event.query)
            is Event.OnBookClicked -> onBookClicked(event.id)
            is Event.OnBookAboutClicked -> onBookAboutClicked(event.id)
            is Event.OnBookRemoveClicked -> onBookRemoveClicked(event.id)
        }
    }

    private fun onSortBooksClicked(sortOrder: SortOrder) {
        this.sortOrder = sortOrder
        loadBooks()
    }

    private fun onQueryTextChanged(searchQuery: String) {
        this.searchQuery = searchQuery
        loadBooks()
    }

    private fun onBookClicked(id: Long) = launch {
        router?.openBook(id)
    }

    private fun onBookAboutClicked(id: Long) = launch {
        router?.openBookDetails(id)
    }

    private fun onBookRemoveClicked(id: Long) = launch {
        runCatching { interactor.removeBook(id) }
                .onSuccess { loadBooks() }
                .onFailure { onAction(Action.Error(R.string.error_book_remove)) }
    }

    sealed class Event {
        data class OnSortBooksClicked(val order: SortOrder) : Event()
        data class OnQueryTextChanged(val query: String) : Event()
        data class OnBookClicked(val id: Long) : Event()
        data class OnBookAboutClicked(val id: Long) : Event()
        data class OnBookRemoveClicked(val id: Long) : Event()
    }

    sealed class Action {
        data class BooksLoaded(val books: List<BookInfo>) : Action()
        data class Error(val error: Int) : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val books: List<BookInfo> = emptyList(),
            val error: Int? = null
    )
}