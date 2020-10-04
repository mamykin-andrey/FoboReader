package ru.mamykin.foboreader.book_details.presentation

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.book_details.domain.BookDetailsInteractor
import ru.mamykin.foboreader.core.mvvm.BaseViewModel

class BookDetailsViewModel(
    private var bookId: Long,
    private val interactor: BookDetailsInteractor,
    private val navigator: BookDetailsNavigator
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    override fun loadData() {
        loadBookInfo()
    }

    private fun loadBookInfo() = launch {
        interactor.getBookInfo(bookId)
            ?.let { sendAction(Action.BookLoaded(it)) }
            ?: sendAction(Action.LoadingError)
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.BookLoaded -> state.copy(isLoading = false, bookInfo = action.bookInfo)
        is Action.LoadingError -> state.copy(isLoading = false, error = true)
    }

    override suspend fun onEvent(event: Event) {
        when (event) {
            is Event.ReadBookClicked -> navigator.bookDetailsToReadBook(bookId)
        }
    }
}