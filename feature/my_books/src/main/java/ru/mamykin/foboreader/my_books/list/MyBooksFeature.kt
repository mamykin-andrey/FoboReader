package ru.mamykin.foboreader.my_books.list

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import ru.mamykin.foboreader.my_books.sort.SortAndFilterBooks
import ru.mamykin.foboreader.my_books.sort.SortOrder
import javax.inject.Inject

internal class MyBooksFeature @Inject constructor(
    actor: MyBooksActor,
    reducer: MyBooksReducer,
    scope: CoroutineScope,
) : ComposeFeature<MyBooksFeature.State, MyBooksFeature.Intent, MyBooksFeature.Effect, MyBooksFeature.Action>(
    State.Loading,
    actor,
    reducer,
    scope,
) {
    internal class MyBooksActor @Inject constructor(
        private val loadMyBooks: LoadMyBooks,
        private val sortAndFilterBooks: SortAndFilterBooks,
        private val removeBook: RemoveBook,
        private val errorMessageMapper: ErrorMessageMapper,
        // TODO: Move to UI
        private val router: Router,
        private val screenProvider: ScreenProvider,
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadBooks -> {
                    emit(Action.BooksLoaded(loadMyBooks.execute()))
                }

                is Intent.RemoveBook -> {
                    removeBook.execute(intent.id).fold(onSuccess = {
                        sendIntent(Intent.LoadBooks)
                    }, onFailure = {
                        emit(Action.RemoveBookError(errorMessageMapper.getMessage(it)))
                    })
                }

                is Intent.SortBooks -> {
                    val state = (state as? State.Content) ?: return@flow
                    emit(createSortAndFilterBooksAction(state.allBooks, intent.sortOrder, state.searchQuery))
                }

                is Intent.FilterBooks -> {
                    val state = (state as? State.Content) ?: return@flow
                    emit(createSortAndFilterBooksAction(state.allBooks, state.sortOrder, intent.searchQuery))
                }

                is Intent.OpenBook -> {
                    router.navigateTo(screenProvider.readBookScreen(intent.bookId))
                }

                is Intent.OpenBookDetails -> {
                    router.navigateTo(screenProvider.bookDetailsScreen(intent.bookId))
                }

                is Intent.ShowSearch -> {
                    emit(Action.ShowSearch)
                }

                is Intent.CloseSearch -> {
                    emit(Action.CloseSearch)
                }
            }
        }

        private fun createSortAndFilterBooksAction(
            allBooks: List<BookInfo>,
            sortOrder: SortOrder,
            searchQuery: String?
        ): Action.BooksUpdated {
            return Action.BooksUpdated(
                allBooks = allBooks,
                books = sortAndFilterBooks.execute(allBooks, sortOrder, searchQuery),
                searchQuery = searchQuery,
                sortOrder = sortOrder,
            )
        }
    }

    internal class MyBooksReducer @Inject constructor() : Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.Loading -> State.Loading to emptySet()

            is Action.BooksUpdated -> {
                getUpdatedBooksState(state, action) to emptySet()
            }

            is Action.RemoveBookError -> state to setOf(Effect.ShowSnackbar(action.error))
            is Action.ShowSearch -> (state as State.Content).copy(searchQuery = "") to emptySet()
            is Action.CloseSearch -> (state as State.Content).copy(searchQuery = null) to emptySet()
        }

        private fun getUpdatedBooksState(state: State, action: Action.BooksUpdated): State.Content {
            return if (state is State.Content) {
                state.copy(
                    books = action.books,
                    searchQuery = action.searchQuery,
                    sortOrder = action.sortOrder,
                )
            } else {
                State.Content(
                    allBooks = action.allBooks,
                    books = action.books,
                    searchQuery = action.searchQuery,
                    sortOrder = action.sortOrder,
                )
            }
        }
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

    sealed class Action {
        data object Loading : Action()
        class BooksLoaded(val books: List<BookInfo>) : Action()
        class BooksUpdated(
            val allBooks: List<BookInfo>,
            val books: List<BookInfo>,
            val searchQuery: String?,
            val sortOrder: SortOrder,
        ) : Action()

        class RemoveBookError(val error: String) : Action()
        data object ShowSearch : Action()
        data object CloseSearch : Action()
    }

    sealed class Effect {
        data class ShowSnackbar(val message: String) : Effect()
    }
}