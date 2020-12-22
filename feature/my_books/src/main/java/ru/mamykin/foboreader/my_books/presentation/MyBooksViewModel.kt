package ru.mamykin.foboreader.my_books.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.my_books.domain.usecase.*

@FlowPreview
@ExperimentalCoroutinesApi
class MyBooksViewModel(
    private val scanBooks: ScanBooks,
    private val getMyBooks: GetMyBooks,
    private val sortMyBooks: SortMyBooks,
    private val filterMyBooks: FilterMyBooks,
    private val removeBook: RemoveBook
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    override fun loadData() {
        launch { scanBooks() }
        getMyBooks()
            .map { Action.BooksLoaded(it) }
            .onEach { sendAction(it) }
            .launchIn(this)
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.Loading -> state.copy(isLoading = true)
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
    }

    override fun onEvent(event: Event) {
        launch {
            when (event) {
                is Event.RemoveBook -> removeBook(event.id)
                is Event.SortBooks -> sortMyBooks(event.sortOrder)
                is Event.FilterBooks -> filterMyBooks(event.query)
            }
        }
    }
}