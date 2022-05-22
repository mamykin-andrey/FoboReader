package ru.mamykin.foboreader.store.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.NotificationId
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
import javax.inject.Named

@BookListScope
internal class BooksListFeature @Inject constructor(
    reducer: BooksListReducer,
    actor: BooksListActor,
) : Feature<BooksListFeature.State, BooksListFeature.Intent, BooksListFeature.Effect, BooksListFeature.Action>(
    State(),
    actor,
    reducer
) {
    init {
        sendIntent(Intent.LoadBooks)
    }

    internal class BooksListActor @Inject constructor(
        private val downloadStoreBook: DownloadBook,
        private val getStoreBooks: GetStoreBooks,
        private val filterStoreBooks: FilterStoreBooks,
        @Named("categoryId")
        private val categoryId: String,
    ) : Actor<Intent, Action> {

        override operator fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadBooks -> {
                    emit(Action.BooksLoadingStarted)
                    getStoreBooks.execute(categoryId).fold(
                        onSuccess = { emit(Action.BooksLoadingSucceed(it)) },
                        onFailure = { emit(Action.BooksLoadingFailed(it)) }
                    )
                }
                is Intent.FilterBooks -> {
                    filterStoreBooks.execute(categoryId, intent.query).fold(
                        onSuccess = { emit(Action.BooksLoadingSucceed(it)) },
                        onFailure = { emit(Action.BooksLoadingFailed(it)) }
                    )
                }
                is Intent.DownloadBook -> {
                    val fileName = intent.fileName
                    emit(Action.DownloadBookStarted(fileName))
                    downloadStoreBook.execute(intent.bookLink, fileName).fold(
                        onSuccess = { emit(Action.BookDownloadSucceed(fileName)) },
                        onFailure = { emit(Action.BookDownloadFailed(fileName)) }
                    )
                }
            }
        }
    }

    internal class BooksListReducer @Inject constructor(
        private val resourceManager: ResourceManager,
        private val errorMessageMapper: ErrorMessageMapper,
    ) : Reducer<State, Action, Effect> {

        private val downloadStarted by lazy { resourceManager.getString(R.string.books_store_download_progress) }
        private val downloadSucceed by lazy { resourceManager.getString(R.string.books_store_download_completed) }
        private val downloadFailed by lazy { resourceManager.getString(R.string.books_store_download_failed) }

        override operator fun invoke(state: State, action: Action) = when (action) {
            is Action.BooksLoadingStarted -> state.copy(
                isLoading = true,
                errorMessage = null,
                books = emptyList()
            ) to emptySet()

            is Action.BooksLoadingSucceed -> state.copy(
                isLoading = false,
                errorMessage = null,
                books = action.books
            ) to emptySet()

            is Action.BooksLoadingFailed -> state.copy(
                isLoading = false,
                errorMessage = errorMessageMapper.getMessage(action.error),
                books = emptyList()
            ) to emptySet()

            is Action.DownloadBookStarted -> state to setOf(
                Effect.ShowNotification(
                    notificationId = NotificationId.FILE_DOWNLOAD,
                    title = downloadStarted,
                    text = action.fileName,
                    iconRes = R.drawable.ic_download,
                )
            )

            is Action.BookDownloadSucceed -> state to setOf(
                Effect.ShowNotification(
                    notificationId = NotificationId.FILE_DOWNLOAD,
                    title = downloadSucceed,
                    text = action.fileName,
                    R.drawable.ic_download,
                ),
                Effect.NavigateToMyBooks,
            )

            is Action.BookDownloadFailed -> state to setOf(
                Effect.ShowNotification(
                    notificationId = NotificationId.FILE_DOWNLOAD,
                    title = downloadFailed,
                    text = action.fileName,
                    R.drawable.ic_download,
                )
            )
        }
    }

    sealed class Intent {
        object LoadBooks : Intent()
        class FilterBooks(val query: String) : Intent()
        class DownloadBook(val bookLink: String, val fileName: String) : Intent()
    }

    sealed class Action {
        object BooksLoadingStarted : Action()
        class BooksLoadingSucceed(val books: List<StoreBook>) : Action()
        class BooksLoadingFailed(val error: Throwable) : Action()
        class DownloadBookStarted(val fileName: String) : Action()
        class BookDownloadSucceed(val fileName: String) : Action()
        class BookDownloadFailed(val fileName: String) : Action()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()
        class ShowNotification(
            val notificationId: Int,
            val title: String,
            val text: String,
            val iconRes: Int,
        ) : Effect()

        // TODO: Use navigation instead of effect?
        object NavigateToMyBooks : Effect()
    }

    data class State(
        val isLoading: Boolean = true,
        val errorMessage: String? = null,
        val books: List<StoreBook>? = null,
    )
}