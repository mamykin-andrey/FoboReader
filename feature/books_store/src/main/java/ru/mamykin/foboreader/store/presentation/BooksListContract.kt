package ru.mamykin.foboreader.store.presentation

import ru.mamykin.foboreader.store.domain.model.StoreBook

object BooksList {

    sealed class Event {
        class FilterQueryChanged(val query: String) : Event()
        class DownloadBookClicked(val bookLink: String, val fileName: String) : Event()
        object RetryBooksClicked : Event()
    }

    sealed class Intent {
        object LoadBooks : Intent()
        class FilterBooks(val query: String) : Intent()
        class DownloadBook(val bookLink: String, val fileName: String) : Intent()
    }

    sealed class Action {
        object BooksLoading : Action()
        class BooksLoaded(val books: List<StoreBook>) : Action()
        class BooksLoadingError(val message: String) : Action()
        object DownloadBookStarted : Action()
        object BookDownloaded : Action()
        class BookDownloadError(val message: String) : Action()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()
        // TODO: Refactor
        object NavigateToMyBooks : Effect()
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val books: List<StoreBook>? = null,
    )
}