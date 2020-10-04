package ru.mamykin.foboreader.store.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.store.domain.interactor.BooksStoreInteractor
import ru.mamykin.foboreader.store.domain.model.StoreBook

@FlowPreview
@ExperimentalCoroutinesApi
class BooksStoreViewModel(
    private val interactor: BooksStoreInteractor,
    private val navigator: BooksStoreNavigator
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    override fun loadData() {
        loadBooks()
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

    override suspend fun onEvent(event: Event) {
        when (event) {
            is Event.FilterBooks -> filterBooks(event.query)
            is Event.DownloadBook -> downloadBook(event.book)
            is Event.RetryBooksLoading -> loadBooks()
        }
    }

    private suspend fun filterBooks(query: String) {
        val books = interactor.filterBooks(query)
        sendAction(Action.BooksLoaded(books))
    }

    private fun loadBooks() = launch {
        sendAction(Action.BooksLoading)
        runCatching { interactor.loadBooks() }
            .onSuccess { sendAction(Action.BooksLoaded(it)) }
            .onFailure { sendAction(Action.BooksLoadingFailed) }
    }

    private fun downloadBook(book: StoreBook) = launch {
        runCatching { interactor.downloadBook(book) }
            .onSuccess { navigator.booksStoreToMyBooksScreen() }
            .onFailure { sendEffect(Effect.ShowSnackbar(it.message!!)) }
    }
}