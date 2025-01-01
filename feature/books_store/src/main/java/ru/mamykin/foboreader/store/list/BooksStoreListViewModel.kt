package ru.mamykin.foboreader.store.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.core.presentation.SnackbarData
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.store.R
import javax.inject.Inject

@HiltViewModel
internal class BooksStoreListViewModel @Inject constructor(
    private val downloadStoreBook: DownloadBook,
    private val getStoreBooks: GetStoreBooks,
    private val errorMessageMapper: ErrorMessageMapper,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val categoryId: String = savedStateHandle.get<String>("categoryId")!!

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()
    private var isDataLoaded = false

    fun sendIntent(intent: Intent) {
        viewModelScope.launch {
            when (intent) {
                is Intent.LoadBooks -> {
                    if (isDataLoaded) return@launch
                    state = State.Loading
                    getStoreBooks.execute(categoryId).fold(
                        onSuccess = {
                            state = State.Content(it)
                            isDataLoaded = true
                        },
                        onFailure = { state = State.Error(errorMessageMapper.getMessage(it)) }
                    )
                }

                is Intent.FilterBooks -> {
                    getStoreBooks.execute(categoryId).fold(
                        onSuccess = { state = State.Content(it) },
                        onFailure = { state = State.Error(errorMessageMapper.getMessage(it)) }
                    )
                }

                is Intent.DownloadBook -> {
                    val fileName = getStorageFileName(intent.book)
                    effectChannel.send(
                        Effect.ShowSnackbar(
                            data = SnackbarData(
                                StringOrResource.Resource(R.string.books_store_download_progress_fmt, fileName)
                            )
                        )
                    )
                    downloadStoreBook.execute(intent.book.link, fileName).fold(
                        onSuccess = {
                            effectChannel.send(Effect.ShowSnackbar(
                                SnackbarData(
                                    message = StringOrResource.Resource(
                                        R.string.books_store_download_completed_fmt,
                                        fileName
                                    ),
                                    action = "Show" to { sendIntent(Intent.OpenMyBooks) }
                                )
                            ))
                        },
                        onFailure = {
                            effectChannel.send(Effect.ShowSnackbar(data = SnackbarData(
                                message = StringOrResource.Resource(
                                    R.string.books_store_download_failed_fmt,
                                    fileName
                                ),
                                action = "Retry" to { sendIntent(Intent.DownloadBook(intent.book)) }
                            )))
                        }
                    )
                }

                is Intent.OpenMyBooks -> {
                    effectChannel.send(Effect.NavigateToMyBooks)
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
        data class FilterBooks(val query: String) : Intent()
        data class DownloadBook(val book: StoreBook) : Intent()
        data object OpenMyBooks : Intent()
    }

    sealed class Effect {
        class ShowSnackbar(val data: SnackbarData) : Effect()
        data object NavigateToMyBooks : Effect()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val books: List<StoreBook>
        ) : State()

        data class Error(
            val message: StringOrResource,
        ) : State()
    }
}