package ru.mamykin.foboreader.store.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.store.domain.model.StoreBook
import ru.mamykin.foboreader.store.domain.usecase.DownloadBook
import ru.mamykin.foboreader.store.domain.usecase.FilterStoreBooks
import ru.mamykin.foboreader.store.domain.usecase.GetStoreBooks
import ru.mamykin.foboreader.store.domain.usecase.LoadStoreBooks
import ru.mamykin.foboreader.store.navigation.BooksStoreNavigator

@FlowPreview
@ExperimentalCoroutinesApi
class BooksStoreViewModel(
    private val downloadBook: DownloadBook,
    private val getStoreBooks: GetStoreBooks,
    private val loadStoreBooks: LoadStoreBooks,
    private val filterStoreBooks: FilterStoreBooks,
    private val navigator: BooksStoreNavigator
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    override fun loadData() {
        sendAction(Action.BooksLoading)
        getStoreBooks.execute()
            .map { Action.BooksLoaded(it) }
            .onEach { sendAction(it) }
            .launchIn(viewModelScope)
        viewModelScope.launch { loadBooks() }
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.BooksLoading -> state.copy(
            isLoading = true,
            isError = false,
            books = emptyList()
        )
        is Action.BooksLoaded -> state.copy(
            isLoading = false,
            isError = false,
            books = action.books
        )
        is Action.BooksLoadingFailed -> state.copy(
            isLoading = false,
            isError = true,
            books = emptyList()
        )
    }

    override fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.FilterBooks -> filterStoreBooks.execute(event.query)
                is Event.DownloadBook -> downloadBook(event.book)
                is Event.RetryBooksLoading -> loadBooks()
            }
        }
    }

    private suspend fun loadBooks() {
        sendAction(Action.BooksLoading)
        runCatching { loadStoreBooks.execute() }
            .onFailure { sendAction(Action.BooksLoadingFailed) }
    }

    private suspend fun downloadBook(book: StoreBook) {
        runCatching { downloadBook.execute(book) }
            .onSuccess { navigator.booksStoreToMyBooksScreen() }
            .onFailure { sendEffect(Effect.ShowSnackbar(it.message!!)) }
    }
}