package ru.mamykin.store.presentation

import kotlinx.coroutines.launch
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.core.platform.Navigator
import ru.mamykin.store.domain.BooksStoreInteractor
import ru.mamykin.store.domain.model.StoreBook

class BooksStoreViewModel(
        private val interactor: BooksStoreInteractor,
        private val navigator: Navigator
) : BaseViewModel<BooksStoreViewModel.ViewState, BooksStoreViewModel.Action, Unit>(
        ViewState(isLoading = true)
) {
    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.LoadingError -> state.copy(isLoading = false, isError = true)
        is Action.DownloadBookError -> state.copy(isLoading = false, isError = true)
    }

    fun loadBooks() = launch {
        runCatching { interactor.getBooks() }
                .onSuccess { onAction(Action.BooksLoaded(it)) }
                .onFailure { onAction(Action.LoadingError) }
    }

    fun downloadBook(book: StoreBook) = launch {
        runCatching { interactor.downloadBook(book) }
                .onSuccess { navigator.openMyBooksScreen() }
                .onFailure { onAction(Action.DownloadBookError) }
    }

    sealed class Action {
        data class BooksLoaded(val books: List<StoreBook>) : Action()
        object LoadingError : Action()
        object DownloadBookError : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val books: List<StoreBook> = emptyList(),
            val isError: Boolean = false
    )
}