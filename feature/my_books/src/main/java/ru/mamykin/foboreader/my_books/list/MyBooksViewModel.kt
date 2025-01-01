package ru.mamykin.foboreader.my_books.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.core.presentation.SnackbarData
import ru.mamykin.foboreader.my_books.sort.SortAndFilterBooks
import ru.mamykin.foboreader.my_books.sort.SortOrder
import javax.inject.Inject

@HiltViewModel
internal class MyBooksViewModel @Inject constructor(
    private val loadMyBooks: LoadMyBooks,
    private val sortAndFilterBooks: SortAndFilterBooks,
    private val removeBook: RemoveBook,
    private val errorMessageMapper: ErrorMessageMapper,
) : ViewModel() {

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()
    private var isDataLoaded = false

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.LoadBooks -> {
                if (isDataLoaded) return@launch
                loadMyBooks.execute().fold(
                    onSuccess = {
                        state = State.Content(allBooks = it, books = it)
                        isDataLoaded = true
                    }, onFailure = {
                        effectChannel.send(Effect.ShowSnackbar(SnackbarData(errorMessageMapper.getMessage(it))))
                    }
                )
            }

            is Intent.RemoveBook -> {
                removeBook.execute(intent.id).fold(onSuccess = {
                    val prevState = state as? State.Content ?: return@launch
                    val updatedBooks = prevState.books.filterNot { it.id == intent.id }
                    state = prevState.copy(books = updatedBooks)
                }, onFailure = {
                    effectChannel.send(Effect.ShowSnackbar(SnackbarData(errorMessageMapper.getMessage(it))))
                })
            }

            is Intent.SortBooks -> {
                val prevState = (state as? State.Content) ?: return@launch
                applySortAndFilter(prevState, intent.sortOrder, prevState.searchQuery)
            }

            is Intent.FilterBooks -> {
                val prevState = (state as? State.Content) ?: return@launch
                applySortAndFilter(prevState, prevState.sortOrder, intent.searchQuery)
            }

            is Intent.OpenBook -> {
                effectChannel.send(Effect.NavigateToReadBook(intent.bookId))
            }

            is Intent.OpenBookDetails -> {
                effectChannel.send(Effect.NavigateToBookDetails(intent.bookId))
            }

            is Intent.ShowSearch -> {
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(searchQuery = "")
            }

            is Intent.CloseSearch -> {
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(searchQuery = null)
            }
        }
    }

    private fun applySortAndFilter(prevState: State.Content, sortOrder: SortOrder, searchQuery: String?) {
        val sortedBooks = sortAndFilterBooks.execute(
            prevState.allBooks,
            sortOrder,
            searchQuery,
        )
        state = prevState.copy(
            books = sortedBooks,
            searchQuery = searchQuery,
            sortOrder = sortOrder,
        )
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val allBooks: List<BookInfo>,
            val books: List<BookInfo>,
            val searchQuery: String? = null,
            val sortOrder: SortOrder = SortOrder.ByName,
        ) : State()
    }

    sealed class Intent {
        data object LoadBooks : Intent()
        data object CloseSearch : Intent()
        data object ShowSearch : Intent()
        class RemoveBook(val id: Long) : Intent()
        class SortBooks(val sortOrder: SortOrder) : Intent()
        class FilterBooks(val searchQuery: String) : Intent()
        class OpenBook(val bookId: Long) : Intent()
        class OpenBookDetails(val bookId: Long) : Intent()
    }

    sealed class Effect {
        data class ShowSnackbar(
            val data: SnackbarData,
        ) : Effect()

        data class NavigateToBookDetails(val bookId: Long) : Effect()
        data class NavigateToReadBook(val bookId: Long) : Effect()
    }
}