package ru.mamykin.foboreader.store.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.store.domain.BooksStoreInteractor
import ru.mamykin.foboreader.store.domain.model.StoreBook

@FlowPreview
@ExperimentalCoroutinesApi
class BooksStoreViewModel(
    private val interactor: BooksStoreInteractor
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    override fun loadData() {
        initBooksFlow()
        launch { interactor.loadBooks() }
    }

    private fun initBooksFlow() = launch {
        interactor.booksFlow.collect {
            sendAction(Action.BooksLoaded(it))
        }
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
    }

    override suspend fun onEvent(event: Event) {
        when (event) {
            is Event.LoadBooks -> interactor.loadBooks()
            is Event.FilterBooks -> interactor.filterBooks(event.query)
            is Event.DownloadBook -> downloadBook(event.book)
        }
    }

    private fun downloadBook(book: StoreBook) = launch {
        runCatching { interactor.downloadBook(book) }
            .onSuccess { sendEffect(Effect.OpenMyBooksScreen) }
            .onFailure { sendEffect(Effect.ShowSnackbar(it.message!!)) }
    }
}