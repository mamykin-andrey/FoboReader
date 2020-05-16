package ru.mamykin.foboreader.store.presentation

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.store.domain.BooksStoreInteractor
import ru.mamykin.foboreader.store.domain.model.StoreBook

@FlowPreview
@ExperimentalCoroutinesApi
class BooksStoreViewModel(
        private val interactor: BooksStoreInteractor,
        private val navigator: Navigator
) : BaseViewModel<BooksStoreViewModel.ViewState, BooksStoreViewModel.Action>(
        ViewState(isLoading = true)
) {
    init {
        launch {
            interactor.booksFlow.collect {
                sendAction(Action.BooksLoaded(it))
            }
        }
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.LoadingError -> state.copy(isLoading = false, isError = true)
        is Action.DownloadBookError -> state.copy(isLoading = false, isError = true)
    }

    fun loadBooks() = launch {
        runCatching { interactor.loadBooks() }
                .onFailure { sendAction(Action.LoadingError) }
    }

    fun filterBooks(query: String) = launch {
        interactor.filterBooks(query)
    }

    fun downloadBook(book: StoreBook) = launch {
        runCatching { interactor.downloadBook(book) }
                .onSuccess { navigator.openMyBooksScreen() }
                .onFailure { sendAction(Action.DownloadBookError) }
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
            // TODO: downloadBookError
    )
}