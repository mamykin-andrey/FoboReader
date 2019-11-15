package ru.mamykin.foboreader.presentation.bookdetails

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.domain.bookdetails.BookDetailsInteractor
import ru.mamykin.foboreader.domain.entity.FictionBook
import javax.inject.Inject

class BookDetailsViewModel @Inject constructor(
        private val interactor: BookDetailsInteractor
) : BaseViewModel<BookDetailsViewModel.ViewState, BookDetailsViewModel.Action, BookDetailsRouter>(
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
        router?.openBook(bookPath)
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