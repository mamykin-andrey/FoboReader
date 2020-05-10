package ru.mamykin.foboreader.my_books.presentation.my_books

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.domain.model.BookInfo
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.my_books.domain.my_books.MyBooksInteractor
import ru.mamykin.foboreader.my_books.domain.my_books.SortOrder

class MyBooksViewModel constructor(
        private val interactor: MyBooksInteractor
) : BaseViewModel<MyBooksViewModel.ViewState, MyBooksViewModel.Action>(
        ViewState(isLoading = true)
) {
    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.Loading -> state.copy(isLoading = true, error = null)
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.Error -> state.copy(isLoading = false, error = action.error)
    }

    fun scanBooks() = launch {
        sendAction(Action.Loading)
        interactor.scanNewFiles(force = true)
        loadBooks()
    }

    fun loadBooks() = launch {
        val books = interactor.getBooks()
        sendAction(Action.BooksLoaded(books))
    }

    fun removeBook(id: Long) = launch {
        interactor.removeBook(id)
        loadBooks()
    }

    fun sortBooks(sortOrder: SortOrder) {
        interactor.sortOrder = sortOrder
        loadBooks()
    }

    fun filterBooks(searchQuery: String) {
        interactor.searchQuery = searchQuery
        loadBooks()
    }

    sealed class Action {
        object Loading : Action()
        data class BooksLoaded(val books: List<BookInfo>) : Action()
        data class Error(val error: Int) : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val books: List<BookInfo> = emptyList(),
            val error: Int? = null
    )
}