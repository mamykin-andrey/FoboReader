package ru.mamykin.store.presentation

import kotlinx.coroutines.launch
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.store.R
import ru.mamykin.store.domain.BooksStoreInteractor
import ru.mamykin.store.domain.FileDownloader
import ru.mamykin.store.domain.model.StoreBook

class BooksStoreViewModel(
        private val interactor: BooksStoreInteractor,
        private val fileDownloader: FileDownloader
) : BaseViewModel<BooksStoreViewModel.ViewState, BooksStoreViewModel.Action, String>(
        ViewState(isLoading = true)
) {
    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.LoadingError -> state.copy(isLoading = false, isError = true)
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.LoadBooks -> loadBooks()
            is Event.BookClicked -> downloadBook(event.book)
        }
    }

    private fun loadBooks() = launch {
        runCatching { interactor.getBooks() }
                .onSuccess { onAction(Action.BooksLoaded(it)) }
                .onFailure { onAction(Action.LoadingError) }
    }

    private fun downloadBook(book: StoreBook) {
        fileDownloader.download(
                book.link,
                book.getBookName(),
                book.title,
                R.string.downloading_book,
                {
                    // TODO: parse book, add it to db and open my_books screen
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        fileDownloader.release()
    }

    sealed class Action {
        data class BooksLoaded(val books: List<StoreBook>) : Action()
        object LoadingError : Action()
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