package ru.mamykin.foboreader.my_books.presentation.my_books

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.domain.model.BookInfo
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.my_books.domain.my_books.MyBooksInteractor
import ru.mamykin.foboreader.my_books.domain.my_books.SortOrder

@FlowPreview
@ExperimentalCoroutinesApi
class MyBooksViewModel constructor(
        private val interactor: MyBooksInteractor
) : BaseViewModel<MyBooksViewModel.ViewState, MyBooksViewModel.Action>(
        ViewState(isLoading = true)
) {
    init {
        launch {
            interactor.booksFlow.collect {
                sendAction(Action.BooksLoaded(it))
            }
        }
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.Loading -> state.copy(isLoading = true, error = null)
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.Error -> state.copy(isLoading = false, error = action.error)
    }

    fun scanBooks() = launch {
        sendAction(Action.Loading)
        interactor.scanBooks()
    }

    fun loadBooks() = launch {
        interactor.loadBooks()
    }

    fun removeBook(id: Long) = launch {
        interactor.removeBook(id)
    }

    fun sortBooks(sortOrder: SortOrder) = launch {
        interactor.sortBooks(sortOrder)
    }

    fun filterBooks(query: String) = launch {
        interactor.filterByQuery(query)
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