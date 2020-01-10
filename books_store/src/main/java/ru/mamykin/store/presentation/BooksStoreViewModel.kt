package ru.mamykin.store.presentation

import androidx.annotation.StringRes
import kotlinx.coroutines.launch
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.store.domain.BooksStoreInteractor
import ru.mamykin.store.domain.DownloadBookException
import ru.mamykin.store.domain.model.StoreBook

class BooksStoreViewModel(
        private val interactor: BooksStoreInteractor
) : BaseViewModel<BooksStoreViewModel.ViewState, BooksStoreViewModel.Action, BooksStoreRouter>(
        ViewState(isLoading = true)
) {
    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.LoadingError -> state.copy(isLoading = false, isError = true)
        is Action.DownloadBookError -> state.copy(isLoading = false, isError = true)
    }

    fun onEvent(event: Event) = when (event) {
        is Event.LoadBooks -> loadBooks()
        is Event.BookClicked -> downloadBook(event.book)
    }

    private fun loadBooks() = launch {
        runCatching { interactor.getBooks() }
                .onSuccess { onAction(Action.BooksLoaded(it)) }
                .onFailure { onAction(Action.LoadingError) }
    }

    private fun downloadBook(book: StoreBook) = launch {
        runCatching { interactor.downloadBook(book) }
                .onSuccess { router?.openMyBooksScreen() }
                .onFailure {
                    val ex = it as DownloadBookException
                    onAction(Action.DownloadBookError(ex.errMsgRes))
                }
    }

    override fun onCleared() {
        super.onCleared()
        interactor.onCleared()
    }

    sealed class Action {
        data class BooksLoaded(val books: List<StoreBook>) : Action()
        object LoadingError : Action()
        data class DownloadBookError(@StringRes val errMsgRes: Int) : Action()
    }

    sealed class Event {
        data class BookClicked(val book: StoreBook) : Event()
        object LoadBooks : Event()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val books: List<StoreBook> = emptyList(),
            val isError: Boolean = false
    )
}