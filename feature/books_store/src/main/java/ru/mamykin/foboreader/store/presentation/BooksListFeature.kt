package ru.mamykin.foboreader.store.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.platform.ResourceManager
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.di.BookListScope
import ru.mamykin.foboreader.store.domain.model.StoreBook
import ru.mamykin.foboreader.store.domain.usecase.DownloadBook
import ru.mamykin.foboreader.store.domain.usecase.FilterStoreBooks
import ru.mamykin.foboreader.store.domain.usecase.GetStoreBooks
import javax.inject.Inject

@BookListScope
internal class BooksListFeature @Inject constructor(
    reducer: BooksListReducer,
    actor: BooksListActor,
    private val uiEventTransformer: UiEventTransformer,
) : Feature<BooksListFeature.State, BooksListFeature.Intent, BooksListFeature.Effect, BooksListFeature.Action>(
    State(),
    actor,
    reducer
) {
    init {
        sendIntent(Intent.LoadBooks)
    }

    fun sendEvent(event: Event) {
        sendIntent(uiEventTransformer.invoke(event))
    }

    internal class BookCategoriesParams(
        val categoryId: String,
    )

    internal class BooksListActor @Inject constructor(
        private val downloadStoreBook: DownloadBook,
        private val getStoreBooks: GetStoreBooks,
        private val filterStoreBooks: FilterStoreBooks,
        private val params: BookCategoriesParams,
    ) : Actor<Intent, Action> {

        override operator fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadBooks -> {
                    emit(Action.BooksLoading)
                    getStoreBooks.execute(params.categoryId).fold(
                        { emit(Action.BooksLoaded(it)) },
                        { emit(Action.BooksLoadingError(it.message.orEmpty())) }
                    )
                }
                is Intent.FilterBooks -> {
                    emit(
                        Action.BooksLoaded(
                            filterStoreBooks.execute(params.categoryId, intent.query).getOrThrow()
                        )
                    )
                }
                is Intent.DownloadBook -> {
                    emit(Action.DownloadBookStarted)
                    downloadStoreBook.execute(intent.bookLink, intent.fileName).fold(
                        { emit(Action.BookDownloaded) },
                        { emit(Action.BookDownloadError(it.message.orEmpty())) }
                    )
                }
            }
        }
    }

    internal class BooksListReducer @Inject constructor(
        private val resourceManager: ResourceManager,
    ) : Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action) =
            when (action) {
                is Action.BooksLoading -> {
                    state.copy(
                        isLoading = true,
                        isError = false,
                        books = emptyList()
                    ) to emptySet()
                }
                is Action.BooksLoaded -> {
                    state.copy(
                        isLoading = false,
                        isError = false,
                        books = action.books
                    ) to emptySet()
                }
                is Action.BooksLoadingError -> {
                    state.copy(
                        isLoading = false,
                        isError = false,
                        books = emptyList()
                    ) to setOf(
                        Effect.ShowSnackbar(action.message)
                    )
                }
                is Action.DownloadBookStarted -> {
                    state to setOf(
                        Effect.ShowSnackbar(resourceManager.getString(R.string.books_store_download_progress))
                    )
                }
                is Action.BookDownloaded -> {
                    state to setOf(
                        Effect.NavigateToMyBooks
                    )
                }
                is Action.BookDownloadError -> {
                    state to setOf(
                        Effect.ShowSnackbar(resourceManager.getString(R.string.books_store_download_failed))
                    )
                }
            }
    }

    internal class UiEventTransformer @Inject constructor() {

        operator fun invoke(event: Event): Intent = when (event) {
            is Event.FilterQueryChanged -> {
                Intent.FilterBooks(event.query)
            }
            is Event.DownloadBookClicked -> {
                Intent.DownloadBook(event.bookLink, event.fileName)
            }
            is Event.RetryBooksClicked -> {
                Intent.LoadBooks
            }
        }
    }

    sealed class Event {
        class FilterQueryChanged(val query: String) : Event()
        class DownloadBookClicked(val bookLink: String, val fileName: String) : Event()
        object RetryBooksClicked : Event()
    }

    sealed class Intent {
        object LoadBooks : Intent()
        class FilterBooks(val query: String) : Intent()
        class DownloadBook(val bookLink: String, val fileName: String) : Intent()
    }

    sealed class Action {
        object BooksLoading : Action()
        class BooksLoaded(val books: List<StoreBook>) : Action()
        class BooksLoadingError(val message: String) : Action()
        object DownloadBookStarted : Action()
        object BookDownloaded : Action()
        class BookDownloadError(val message: String) : Action()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()

        // TODO: Refactor
        object NavigateToMyBooks : Effect()
    }

    data class State(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val books: List<StoreBook>? = null,
    )
}