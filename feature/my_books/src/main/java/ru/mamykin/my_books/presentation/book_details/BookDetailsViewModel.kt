package ru.mamykin.my_books.presentation.book_details

import kotlinx.coroutines.launch
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.my_books.domain.book_details.BookDetailsInteractor
import ru.mamykin.my_books.domain.model.BookInfo

class BookDetailsViewModel constructor(
        private val interactor: BookDetailsInteractor
) : BaseViewModel<BookDetailsViewModel.ViewState, BookDetailsViewModel.Action>(
        ViewState(isLoading = true)
) {
    private var bookId: Long = -1

    fun loadData(bookId: Long) {
        this.bookId = bookId
        loadBookInfo()
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BookLoaded -> state.copy(isLoading = false, bookInfo = action.bookInfo)
        is Action.LoadingError -> state.copy(isLoading = false, error = true)
    }

    private fun onReadBookClicked() {
        // TODO: router?.openBook(bookPath)
    }

    private fun loadBookInfo() = launch {
        interactor.getBookInfo(bookId)
                ?.let { onAction(Action.BookLoaded(it)) }
                ?: onAction(Action.LoadingError)
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
        data class BookLoaded(val bookInfo: BookInfo) : Action()
        object LoadingError : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val bookInfo: BookInfo? = null,
            val error: Boolean = false
    )
}