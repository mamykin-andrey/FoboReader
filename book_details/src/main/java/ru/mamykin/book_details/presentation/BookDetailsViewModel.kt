package ru.mamykin.book_details.presentation

import kotlinx.coroutines.launch
import ru.mamykin.book_details.domain.BookDetailsInteractor
import ru.mamykin.core.data.model.FictionBook
import ru.mamykin.core.mvvm.BaseViewModel

class BookDetailsViewModel constructor(
        private val interactor: BookDetailsInteractor
) : BaseViewModel<BookDetailsViewModel.ViewState, BookDetailsViewModel.Action, String>(
        ViewState(isLoading = true)
) {
    private lateinit var bookPath: String

    fun loadData(bookPath: String) {
        this.bookPath = bookPath
        loadBookInfo()
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BookLoaded -> state.copy(isLoading = false, book = action.book)
        is Action.LoadingError -> state.copy(isLoading = false, error = true)
    }

    private fun onReadBookClicked() {
        // TODO: router?.openBook(bookPath)
    }

    private fun loadBookInfo() = launch {
        runCatching { interactor.getBookInfo(bookPath) }
                .onSuccess { onAction(Action.BookLoaded(it)) }
                .onFailure { onAction(Action.LoadingError) }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnReadBookClicked -> onReadBookClicked()
        }
    }

    sealed class Event {
        object OnReadBookClicked : Event()
    }

    sealed class Action {
        data class BookLoaded(val book: FictionBook) : Action()
        object LoadingError : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val book: FictionBook? = null,
            val error: Boolean = false
    )
}