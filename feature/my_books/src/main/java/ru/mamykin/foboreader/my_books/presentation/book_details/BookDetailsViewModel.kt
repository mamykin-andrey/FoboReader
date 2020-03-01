package ru.mamykin.foboreader.my_books.presentation.book_details

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.domain.model.BookInfo
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.my_books.domain.book_details.BookDetailsInteractor

class BookDetailsViewModel(
        private val interactor: BookDetailsInteractor,
        private val navigator: Navigator
) : BaseViewModel<BookDetailsViewModel.ViewState, BookDetailsViewModel.Action>(
        ViewState(isLoading = true)
) {
    private var bookId: Long = -1

    fun loadBookInfo(bookId: Long) {
        this.bookId = bookId
        loadBookInfo()
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BookLoaded -> state.copy(isLoading = false, bookInfo = action.bookInfo)
        is Action.LoadingError -> state.copy(isLoading = false, error = true)
    }

    fun onReadBookClicked() {
        navigator.openBook(bookId)
    }

    private fun loadBookInfo() = launch {
        interactor.getBookInfo(bookId)
                ?.let { sendAction(Action.BookLoaded(it)) }
                ?: sendAction(Action.LoadingError)
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