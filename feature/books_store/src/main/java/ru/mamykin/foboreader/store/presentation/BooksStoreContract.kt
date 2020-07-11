package ru.mamykin.foboreader.store.presentation

import ru.mamykin.foboreader.store.domain.model.StoreBook

sealed class Action {
    data class BooksLoaded(val books: List<StoreBook>) : Action()
}

sealed class Event {
    object LoadBooks : Event()
    data class FilterBooks(val query: String) : Event()
    data class DownloadBook(val book: StoreBook) : Event()
}

sealed class Effect {
    data class ShowSnackbar(val message: String) : Effect()
    object OpenMyBooksScreen : Effect()
}

// TODO: downloadBookError
data class ViewState(
    val isLoading: Boolean = false,
    val books: List<StoreBook> = emptyList()
)