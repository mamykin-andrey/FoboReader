package ru.mamykin.foboreader.my_books.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.my_books.domain.usecase.*

class MyBooksViewModel(
    private val scanBooks: ScanBooks,
    getMyBooks: GetMyBooks,
    private val sortMyBooks: SortMyBooks,
    private val filterMyBooks: FilterMyBooks,
    private val removeBook: RemoveBook
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    init {
        viewModelScope.launch { scanBooks(Unit) }
        getMyBooks()
            .onEach { sendAction(Action.BooksLoaded(it.getOrThrow())) }
            .launchIn(viewModelScope)
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.Loading -> state.copy(isLoading = true)
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
    }

    override fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.RemoveBook -> removeBook(event.id)
                is Event.SortBooks -> sortMyBooks(event.sortOrder)
                is Event.FilterBooks -> filterMyBooks(event.query)
            }
        }
    }
}