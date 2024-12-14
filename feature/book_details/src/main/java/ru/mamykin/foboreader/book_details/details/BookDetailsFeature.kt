package ru.mamykin.foboreader.book_details.details

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import javax.inject.Inject
import javax.inject.Named

internal class BookDetailsFeature @Inject constructor(
    actor: BookDetailsActor,
    reducer: BookDetailsReducer,
    scope: CoroutineScope,
) : ComposeFeature<BookDetailsFeature.State, BookDetailsFeature.Intent, BookDetailsFeature.Effect, BookDetailsFeature.Action>(
    State.Loading,
    actor,
    reducer,
    scope,
) {
    internal class BookDetailsActor @Inject constructor(
        @Named("bookId") private val bookId: Long,
        private val getBookDetails: GetBookDetails,
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.OpenBook -> {
                    emit(Action.BookOpened(bookId))
                }

                is Intent.LoadBookInfo -> {
                    emit(Action.BookLoaded(getBookDetails.execute(bookId)))
                }
            }
        }
    }

    internal class BookDetailsReducer @Inject constructor() : Reducer<State, Action, Effect> {

        override fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.BookLoaded -> State.Loaded(
                bookDetails = action.bookDetails,
            ) to emptySet()

            is Action.BookOpened -> state to setOf(Effect.NavigateToReadBook(action.bookId))
        }
    }

    sealed class Intent {
        data object LoadBookInfo : Intent()
        data object OpenBook : Intent()
    }

    sealed class Action {
        data class BookLoaded(val bookDetails: BookDetails) : Action()
        data class BookOpened(val bookId: Long) : Action()
    }

    sealed class State {
        data object Loading : State()
        data class Loaded(
            val bookDetails: BookDetails,
        ) : State()
    }

    sealed class Effect {
        data class NavigateToReadBook(val bookId: Long) : Effect()
    }
}