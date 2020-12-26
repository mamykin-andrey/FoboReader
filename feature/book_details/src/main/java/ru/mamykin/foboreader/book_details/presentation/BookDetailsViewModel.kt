package ru.mamykin.foboreader.book_details.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.book_details.domain.usecase.GetBookDetails
import ru.mamykin.foboreader.book_details.navigation.BookDetailsNavigator
import ru.mamykin.foboreader.core.presentation.BaseViewModel

class BookDetailsViewModel(
    private var bookId: Long,
    private val getBookDetails: GetBookDetails,
    private val navigator: BookDetailsNavigator
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isLoading = true)
) {
    init {
        viewModelScope.launch {
            getBookDetails(bookId)
                .doOnSuccess { sendAction(Action.BookLoaded(it)) }
                .doOnError { sendAction(Action.LoadingError) }
        }
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.BookLoaded -> state.copy(isLoading = false, bookInfo = action.bookInfo)
        is Action.LoadingError -> state.copy(isLoading = false, error = true)
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.ReadBookClicked -> navigator.bookDetailsToReadBook(bookId)
        }
    }
}