package ru.mamykin.foboreader.store.list

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.core.presentation.SnackbarAction
import ru.mamykin.foboreader.core.presentation.SnackbarData
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.store.R
import javax.inject.Inject

@HiltViewModel
internal class StoreBooksViewModel @Inject constructor(
    private val downloadStoreBookUseCase: DownloadBookUseCase,
    private val getStoreBooksUseCase: GetStoreBooksUseCase,
    private val errorMessageMapper: ErrorMessageMapper,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<StoreBooksViewModel.Intent, StoreBooksViewModel.State, StoreBooksViewModel.Effect>(
    State.Loading
) {
    private val categoryId: String = savedStateHandle.get<String>("categoryId")!!
    private var allBooks: List<StoreBookEntity> = emptyList()

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadBooks -> loadBooks()

            is Intent.FilterBooks -> {
                getStoreBooksUseCase.execute(categoryId).fold(
                    onSuccess = { state = State.Content(it.map(StoreBookUIModel::fromDomainModel)) },
                    onFailure = { state = State.Error(errorMessageMapper.getMessage(it)) }
                )
            }

            is Intent.DownloadBook -> downloadBook(intent)
            is Intent.OpenMyBooks -> sendEffect(Effect.NavigateToMyBooks)
        }
    }

    private suspend fun downloadBook(intent: Intent.DownloadBook) {
        val book = allBooks.find { it.id == intent.bookId } ?: return

        showSnackbar(StringOrResource.Resource(R.string.books_store_download_progress_fmt, book.title))
        downloadStoreBookUseCase.execute(book).fold(
            onSuccess = {
                showSnackbar(
                    message = StringOrResource.Resource(
                        R.string.books_store_download_completed_fmt,
                        book.title,
                    ),
                    action = "Show" to { sendIntent(Intent.OpenMyBooks) }
                )
            },
            onFailure = {
                showSnackbar(
                    StringOrResource.Resource(R.string.books_store_download_failed_fmt, book.title),
                    "Retry" to { sendIntent(Intent.DownloadBook(intent.bookId)) }
                )
            }
        )
    }

    private suspend fun loadBooks() {
        if (allBooks.isNotEmpty()) return

        state = State.Loading
        getStoreBooksUseCase.execute(categoryId).fold(
            onSuccess = { books ->
                this.allBooks = books
                state = State.Content(books.map(StoreBookUIModel::fromDomainModel))
            },
            onFailure = {
                state = State.Error(errorMessageMapper.getMessage(it))
            }
        )
    }

    private suspend fun showSnackbar(message: StringOrResource, action: SnackbarAction? = null) {
        sendEffect(
            Effect.ShowSnackbar(
                SnackbarData(
                    message = message,
                    action = action,
                )
            )
        )
    }

    sealed class Intent {
        data object LoadBooks : Intent()
        data class FilterBooks(val query: String) : Intent()
        data class DownloadBook(val bookId: String) : Intent()
        data object OpenMyBooks : Intent()
    }

    sealed class Effect {
        class ShowSnackbar(val data: SnackbarData) : Effect()
        data object NavigateToMyBooks : Effect()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val books: List<StoreBookUIModel>
        ) : State()

        data class Error(
            val message: StringOrResource,
        ) : State()
    }
}