package ru.mamykin.foboreader.store.presentation

import ru.mamykin.foboreader.core.presentation.Reducer

class BooksStoreReducer : Reducer<BooksStore.ViewState, BooksStore.Action, BooksStore.Effect> {

    override operator fun invoke(state: BooksStore.ViewState, action: BooksStore.Action) = when (action) {
        is BooksStore.Action.BooksLoading -> {
            state.copy(
                isLoading = true,
                isError = false,
                books = emptyList()
            ) to emptySet()
        }
        is BooksStore.Action.BooksLoaded -> {
            state.copy(
                isLoading = false,
                isError = false,
                books = action.books
            ) to emptySet()
        }
        is BooksStore.Action.BooksLoadingError -> {
            state.copy(
                isLoading = false,
                isError = false,
                books = emptyList()
            ) to setOf(
                BooksStore.Effect.ShowSnackbar(action.message)
            )
        }
        is BooksStore.Action.DownloadBookStarted -> {
            state to setOf(
                BooksStore.Effect.ShowSnackbar("Загрузка началась")
            )
        }
        is BooksStore.Action.BookDownloaded -> {
            state to setOf(
                BooksStore.Effect.NavigateToMyBooks
            )
        }
        is BooksStore.Action.BookDownloadError -> {
            state to setOf(
                BooksStore.Effect.ShowSnackbar("Ошибка загрузки: ${action.message}")
            )
        }
    }
}