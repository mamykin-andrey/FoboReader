package ru.mamykin.store.presentation

import kotlinx.coroutines.launch
import ru.mamykin.core.mvvm.BaseViewModel
import ru.mamykin.store.domain.BooksStoreInteractor
import ru.mamykin.store.domain.entity.StoreBook
import javax.inject.Inject

class BooksStoreViewModel @Inject constructor(
        private val interactor: BooksStoreInteractor
) : BaseViewModel<BooksStoreViewModel.ViewState, BooksStoreViewModel.Action, String>(
        ViewState(isLoading = true)
) {
    fun loadBooks() = launch {
        runCatching { interactor.getBooks() }
                .onSuccess { onAction(Action.BooksLoaded(it)) }
                .onFailure { onAction(Action.LoadingError) }
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books)
        is Action.LoadingError -> state.copy(isLoading = false, isError = true)
    }

    sealed class Action {
        data class BooksLoaded(val books: List<StoreBook>) : Action()
        object LoadingError : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val books: List<StoreBook> = emptyList(),
            val isError: Boolean = false
    )
}