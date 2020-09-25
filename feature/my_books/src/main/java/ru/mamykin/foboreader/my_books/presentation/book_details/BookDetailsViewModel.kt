package ru.mamykin.foboreader.my_books.presentation.book_details

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.my_books.domain.book_details.BookDetailsInteractor

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
            is Event.ReadBookClicked -> navigator.openBook(bookId)
        }
    }
}