package ru.mamykin.my_books.presentation.my_books

import kotlinx.coroutines.launch
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.core.platform.Navigator
import ru.mamykin.my_books.R
import ru.mamykin.my_books.domain.model.BookInfo
import ru.mamykin.my_books.domain.my_books.MyBooksInteractor
import ru.mamykin.my_books.domain.my_books.SortOrder
import ru.mamykin.my_books.presentation.my_books.list.BookAction

class MyBooksViewModel constructor(
        private val interactor: MyBooksInteractor,
        private val navigator: Navigator
) : BaseViewModel<MyBooksViewModel.ViewState, MyBooksViewModel.Action, Unit>(
        ViewState(isLoading = true)
) {
    private var searchQuery: String = ""
    private var sortOrder: SortOrder = SortOrder.ByName

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.Error -> state.copy(isLoading = false, error = action.error)
    }

    fun loadBooks() = launch {
        runCatching { interactor.getBooks(searchQuery, sortOrder) }
                .onSuccess { onAction(Action.BooksLoaded(it)) }
                .onFailure {
                    it.printStackTrace()
                    onAction(Action.Error(R.string.error_book_list_loading))
                }
    }

    fun onBookAction(action: BookAction, id: Long) {
        when (action) {
            is BookAction.Open -> navigator.openBook(id)
            is BookAction.About -> navigator.openBookDetails(id)
            is BookAction.Remove -> removeBook(id)
        }
    }

    private fun removeBook(id: Long) = launch {
        runCatching { interactor.removeBook(id) }
                .onSuccess { loadBooks() }
                .onFailure { onAction(Action.Error(R.string.error_book_remove)) }
    }

    fun sortBooks(sortOrder: SortOrder) {
        this.sortOrder = sortOrder
        loadBooks()
    }

    fun filterBooks(searchQuery: String) {
        this.searchQuery = searchQuery
        loadBooks()
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