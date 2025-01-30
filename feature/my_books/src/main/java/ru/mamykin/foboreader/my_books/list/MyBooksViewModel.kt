package ru.mamykin.foboreader.my_books.list

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.core.presentation.SnackbarData
import ru.mamykin.foboreader.my_books.sort.SortAndFilterBooks
import ru.mamykin.foboreader.my_books.sort.SortOrder
import javax.inject.Inject

@HiltViewModel
internal class MyBooksViewModel @Inject constructor(
    private val getMyBooksUseCase: GetMyBooksUseCase,
    private val sortAndFilterBooks: SortAndFilterBooks,
    private val removeBookUseCase: RemoveBookUseCase,
    private val errorMessageMapper: ErrorMessageMapper,
    private val myBookUIModelMapper: BookInfoUIModelMapper,
) : BaseViewModel<MyBooksViewModel.Intent, MyBooksViewModel.State, MyBooksViewModel.Effect>(State.Loading) {

    private var allBooks: List<DownloadedBookEntity>? = null

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadBooks -> {
                val books = getMyBooksUseCase.execute()
                this@MyBooksViewModel.allBooks = books
                state = State.Content(books = books.map(myBookUIModelMapper::map))
            }

            is Intent.RemoveBook -> {
                removeBookUseCase.execute(intent.id).fold(onSuccess = {
                    val prevState = state as? State.Content ?: return
                    val updatedBooks = prevState.books.filterNot { it.id == intent.id }
                    state = prevState.copy(books = updatedBooks)
                }, onFailure = {
                    sendEffect(Effect.ShowSnackbar(SnackbarData(errorMessageMapper.getMessage(it))))
                })
            }

            is Intent.SortBooks -> {
                val prevState = (state as? State.Content) ?: return
                applySortAndFilter(prevState, intent.sortOrder, prevState.searchQuery)
            }

            is Intent.FilterBooks -> {
                val prevState = (state as? State.Content) ?: return
                applySortAndFilter(prevState, prevState.sortOrder, intent.searchQuery)
            }

            is Intent.OpenBook -> {
                sendEffect(Effect.NavigateToReadBook(intent.bookId))
            }

            is Intent.OpenBookDetails -> {
                sendEffect(Effect.NavigateToBookDetails(intent.bookId))
            }

            is Intent.ShowSearch -> {
                val prevState = (state as? State.Content) ?: return
                state = prevState.copy(searchQuery = "")
            }

            is Intent.CloseSearch -> {
                val prevState = (state as? State.Content) ?: return
                applySortAndFilter(prevState, prevState.sortOrder, null)
            }
        }
    }

    private fun applySortAndFilter(prevState: State.Content, sortOrder: SortOrder, searchQuery: String?) {
        val allBooks = this.allBooks ?: return
        val sortedBooks = sortAndFilterBooks.execute(
            allBooks,
            sortOrder,
            searchQuery,
        ).map(myBookUIModelMapper::map)
        state = prevState.copy(
            books = sortedBooks,
            searchQuery = searchQuery,
            sortOrder = sortOrder,
        )
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val books: List<BookInfoUIModel>,
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