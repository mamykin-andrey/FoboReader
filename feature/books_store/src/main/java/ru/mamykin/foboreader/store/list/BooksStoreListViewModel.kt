package ru.mamykin.foboreader.store.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.ResourceManager
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.store.R
import javax.inject.Inject
import javax.inject.Named

@BookListScope
internal class BooksStoreListViewModel @Inject constructor(
    @Named("categoryId")
    private val categoryId: String,
    private val downloadStoreBook: DownloadBook,
    private val getStoreBooks: GetStoreBooks,
    private val filterStoreBooks: FilterStoreBooks,
    private val resourceManager: ResourceManager,
    private val errorMessageMapper: ErrorMessageMapper,
) : ViewModel() {

    // TODO: Replace with StringOrResource
    private val downloadStarted by lazy { resourceManager.getString(R.string.books_store_download_progress) }
    private val downloadSucceed by lazy { resourceManager.getString(R.string.books_store_download_completed) }
    private val downloadFailed by lazy { resourceManager.getString(R.string.books_store_download_failed) }

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.LoadBooks -> {
                    state = State.Loading
                    getStoreBooks.execute(categoryId).fold(
                        onSuccess = { state = State.Content(it) },
                        onFailure = { state = State.Error(errorMessageMapper.getMessage(it)) }
                    )
                }

                is Intent.FilterBooks -> {
                    filterStoreBooks.execute(categoryId, intent.query).fold(
                        onSuccess = { state = State.Content(it) },
                        onFailure = { state = State.Error(errorMessageMapper.getMessage(it)) }
                    )
                }

                is Intent.DownloadBook -> {
                    val fileName = getStorageFileName(intent.book)
                    effectChannel.send(
                        Effect.ShowSnackbar(message = "$downloadStarted: $fileName")
                    )
                    downloadStoreBook.execute(intent.book.link, fileName).fold(
                        onSuccess = {
                            effectChannel.send(
                                Effect.ShowSnackbar(
                                    message = "$downloadSucceed: $fileName",
                                    action = "Show" to { effectChannel.trySend(Effect.NavigateToMyBooks) }
                                )
                            )
                        },
                        onFailure = {
                            effectChannel.send(
                                Effect.ShowSnackbar(
                                    message = "$downloadFailed: $fileName",
                                    action = "Retry" to { sendIntent(Intent.DownloadBook(intent.book)) }
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    private fun getStorageFileName(book: StoreBook): String {
        val transliteratedName = StringTransliterator.transliterate(book.title)
        return "$transliteratedName.${book.format}"
    }

    sealed class Intent {
        data object LoadBooks : Intent()
        class FilterBooks(val query: String) : Intent()
        class DownloadBook(val book: StoreBook) : Intent()
    }

    sealed class Effect {
        class ShowSnackbar(
            val message: String,
            val action: Pair<String, () -> Unit>? = null,
        ) : Effect()

        data object NavigateToMyBooks : Effect()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val books: List<StoreBook>
        ) : State()

        data class Error(
            val message: String
        ) : State()
    }
}