package ru.mamykin.foboreader.my_books.list

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
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
) : ComposeFeature<MyBooksFeature.State, MyBooksFeature.Intent, MyBooksFeature.Effect, MyBooksFeature.Action>(
    State.Loading,
    actor,
    reducer,
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
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadBooks -> loadBooks(true)
                is Intent.RemoveBook -> {
                    removeBook.execute(intent.id).fold(onSuccess = { loadBooks(true) }, onFailure = {
                        emit(Action.RemoveBookError(errorMessageMapper.getMessage(it)))
                    })
                }

                is Intent.SortBooks -> {
                    emit(Action.BooksLoaded(sortMyBooks.execute(intent.sortOrder)))
                }

                is Intent.FilterBooks -> {
                    emit(Action.BooksLoaded(filterMyBooks.execute(intent.query)))
                }
            }
        }

        private suspend fun FlowCollector<Action>.loadBooks(force: Boolean) {
            emit(Action.BooksLoaded(getMyBooks.execute(force)))
        }
    }

    internal class MyBooksReducer @Inject constructor() : Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.Loading -> State.Loading to emptySet()
            is Action.BooksLoaded -> State.Content(action.books) to emptySet()
            is Action.RemoveBookError -> state to setOf(Effect.ShowSnackbar(action.error))
        }
    }

    sealed class State {
        object Loading : State()

        data class Content(
            val books: List<BookInfo>
        ) : State()
    }

    sealed class Intent {
        object LoadBooks : Intent()
        class RemoveBook(val id: Long) : Intent()
        class SortBooks(val sortOrder: SortOrder) : Intent()
        class FilterBooks(val query: String) : Intent()
    }

    sealed class Action {
        object Loading : Action()
        class BooksLoaded(val books: List<BookInfo>) : Action()
        class RemoveBookError(val error: String) : Action()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()
    }
}