package ru.mamykin.foboreader.store.presentation

import ru.mamykin.foboreader.store.domain.model.StoreBook

sealed class Action {
    object BooksLoading : Action()
    data class BooksLoaded(val books: List<StoreBook>) : Action()
    object BooksLoadingFailed : Action()
}

sealed class Event {
    data class FilterBooks(val query: String) : Event()
    data class DownloadBook(val book: StoreBook) : Event()
    object RetryBooksLoading : Event()
}

sealed class Effect {
    data class ShowSnackbar(val message: String) : Effect()
}

// TODO: downloadBookError
data class ViewState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val books: List<StoreBook> = emptyList()
)