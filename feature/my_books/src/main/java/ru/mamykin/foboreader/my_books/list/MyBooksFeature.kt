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
import ru.mamykin.foboreader.my_books.search.FilterMyBooks
import ru.mamykin.foboreader.my_books.sort.SortMyBooks
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
    init {
        sendIntent(Intent.LoadBooks)
    }

    internal class MyBooksActor @Inject constructor(
        private val getMyBooks: GetMyBooks,
        private val sortMyBooks: SortMyBooks,
        private val filterMyBooks: FilterMyBooks,
        private val removeBook: RemoveBook,
        private val errorMessageMapper: ErrorMessageMapper,
        private val router: Router,
        private val screenProvider: ScreenProvider,
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadBooks -> {
                    emit(Action.BooksLoaded(getMyBooks.execute(force = true)))
                }

                is Intent.RemoveBook -> {
                    removeBook.execute(intent.id).fold(onSuccess = {
                        emit(Action.BooksLoaded(getMyBooks.execute(force = true)))
                    }, onFailure = {
                        emit(Action.RemoveBookError(errorMessageMapper.getMessage(it)))
                    })
                }

                is Intent.SortBooks -> {
                    emit(Action.BooksLoaded(sortMyBooks.execute(intent.sortOrder)))
                }

                is Intent.FilterBooks -> {
                    emit(Action.BooksFiltered(filterMyBooks.execute(intent.query), searchQuery = intent.query))
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
    }

    internal class MyBooksReducer @Inject constructor() : Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.Loading -> State.Loading to emptySet()
            is Action.BooksLoaded -> State.Content(action.books) to emptySet()
            is Action.BooksFiltered -> {
                val prevState = state as State.Content
                prevState.copy(books = action.books, searchQuery = action.searchQuery) to emptySet()
            }

            is Action.RemoveBookError -> state to setOf(Effect.ShowSnackbar(action.error))
            is Action.ShowSearch -> (state as State.Content).copy(searchQuery = "") to emptySet()
            is Action.CloseSearch -> (state as State.Content).copy(searchQuery = null) to emptySet()
        }
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val books: List<BookInfo>,
            val searchQuery: String? = null,
        ) : State()
    }

    sealed class Intent {
        data object LoadBooks : Intent()
        data object CloseSearch : Intent()
        data object ShowSearch : Intent()
        class RemoveBook(val id: Long) : Intent()
        class SortBooks(val sortOrder: SortOrder) : Intent()
        class FilterBooks(val query: String) : Intent()
        class OpenBook(val bookId: Long) : Intent()
        class OpenBookDetails(val bookId: Long) : Intent()
    }

    sealed class Action {
        data object Loading : Action()
        class BooksLoaded(val books: List<BookInfo>) : Action()
        class BooksFiltered(val books: List<BookInfo>, val searchQuery: String) : Action()
        class RemoveBookError(val error: String) : Action()
        data object ShowSearch : Action()
        data object CloseSearch : Action()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()
    }
}