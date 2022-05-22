package ru.mamykin.foboreader.my_books.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import ru.mamykin.foboreader.my_books.domain.usecase.FilterMyBooks
import ru.mamykin.foboreader.my_books.domain.usecase.GetMyBooks
import ru.mamykin.foboreader.my_books.domain.usecase.RemoveBook
import ru.mamykin.foboreader.my_books.domain.usecase.SortMyBooks
import javax.inject.Inject

internal class MyBooksFeature @Inject constructor(
    actor: MyBooksActor,
    reducer: MyBooksReducer,
    private val uiTransformer: MyBooksUiTransformer,
) : Feature<MyBooksFeature.State, MyBooksFeature.Intent, MyBooksFeature.Effect, MyBooksFeature.Action>(
    State(isLoading = true, books = null, error = null),
    actor,
    reducer,
) {
    init {
        sendIntent(Intent.LoadBooks)
    }

    fun sendEvent(event: Event) {
        sendIntent(uiTransformer.invoke(event))
    }

    internal class MyBooksUiTransformer @Inject constructor() {

        operator fun invoke(event: Event): Intent = when (event) {
            is Event.FilterTextChanged -> Intent.FilterBooks(event.query)
        }
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
                    removeBook.execute(intent.id).fold(
                        onSuccess = { loadBooks(true) },
                        onFailure = {
                            emit(Action.RemoveBookError(errorMessageMapper.getMessage(it)))
                        }
                    )
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
            is Action.Loading -> state.copy(isLoading = true, books = null) to emptySet()
            is Action.BooksLoaded -> state.copy(isLoading = false, books = action.books) to emptySet()
            is Action.RemoveBookError -> state to setOf(Effect.ShowSnackbar(action.error))
        }
    }

    data class State(
        val isLoading: Boolean,
        val books: List<BookInfo>?,
        val error: String?,
    )

    sealed class Event {
        class FilterTextChanged(val query: String) : Event()
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