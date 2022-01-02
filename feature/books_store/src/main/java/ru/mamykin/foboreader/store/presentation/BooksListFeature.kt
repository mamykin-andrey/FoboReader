package ru.mamykin.foboreader.store.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.store.domain.usecase.DownloadBook
import ru.mamykin.foboreader.store.domain.usecase.FilterStoreBooks
import ru.mamykin.foboreader.store.domain.usecase.GetStoreBooks
import javax.inject.Inject

internal class BooksListFeature @Inject constructor(
    reducer: BooksListReducer,
    actor: BooksListActor,
    private val uiEventTransformer: UiEventTransformer
) : Feature<BooksList.ViewState, BooksList.Intent, BooksList.Effect, BooksList.Action, Nothing>(
    BooksList.ViewState(),
    actor,
    reducer
) {
    init {
        sendIntent(BooksList.Intent.LoadBooks)
    }

    fun sendEvent(event: BooksList.Event) {
        sendIntent(uiEventTransformer.invoke(event))
    }

    internal class BooksListActor @Inject constructor(
        private val downloadStoreBook: DownloadBook,
        private val getStoreBooks: GetStoreBooks,
        private val filterStoreBooks: FilterStoreBooks
    ) : Actor<BooksList.Intent, BooksList.Action> {

        override operator fun invoke(intent: BooksList.Intent): Flow<BooksList.Action> = flow {
            when (intent) {
                is BooksList.Intent.LoadBooks -> {
                    emit(BooksList.Action.BooksLoading)
                    getStoreBooks(Unit).catchMap(
                        { emit(BooksList.Action.BooksLoaded(it)) },
                        { emit(BooksList.Action.BooksLoadingError(it.message.orEmpty())) }
                    )
                }
                is BooksList.Intent.FilterBooks -> {
                    emit(
                        BooksList.Action.BooksLoaded(
                            filterStoreBooks(intent.query).getOrThrow()
                        )
                    )
                }
                is BooksList.Intent.DownloadBook -> {
                    emit(BooksList.Action.DownloadBookStarted)
                    downloadStoreBook(intent.book).catchMap(
                        { emit(BooksList.Action.BookDownloaded) },
                        { emit(BooksList.Action.BookDownloadError(it.message.orEmpty())) }
                    )
                }
            }
        }
    }

    internal class BooksListReducer @Inject constructor() :
        Reducer<BooksList.ViewState, BooksList.Action, BooksList.Effect> {

        override operator fun invoke(state: BooksList.ViewState, action: BooksList.Action) = when (action) {
            is BooksList.Action.BooksLoading -> {
                state.copy(
                    isLoading = true,
                    isError = false,
                    books = emptyList()
                ) to emptySet()
            }
            is BooksList.Action.BooksLoaded -> {
                state.copy(
                    isLoading = false,
                    isError = false,
                    books = action.books
                ) to emptySet()
            }
            is BooksList.Action.BooksLoadingError -> {
                state.copy(
                    isLoading = false,
                    isError = false,
                    books = emptyList()
                ) to setOf(
                    BooksList.Effect.ShowSnackbar(action.message)
                )
            }
            is BooksList.Action.DownloadBookStarted -> {
                state to setOf(
                    BooksList.Effect.ShowSnackbar("Загрузка началась")
                )
            }
            is BooksList.Action.BookDownloaded -> {
                state to setOf(
                    BooksList.Effect.NavigateToMyBooks
                )
            }
            is BooksList.Action.BookDownloadError -> {
                state to setOf(
                    BooksList.Effect.ShowSnackbar("Ошибка загрузки: ${action.message}")
                )
            }
        }
    }

    internal class UiEventTransformer @Inject constructor() {

        operator fun invoke(event: BooksList.Event): BooksList.Intent = when (event) {
            is BooksList.Event.FilterQueryChanged -> {
                BooksList.Intent.FilterBooks(event.query)
            }
            is BooksList.Event.DownloadBookClicked -> {
                BooksList.Intent.DownloadBook(event.book)
            }
            is BooksList.Event.RetryBooksClicked -> {
                BooksList.Intent.LoadBooks
            }
        }
    }
}