package ru.mamykin.foboreader.book_details.presentation

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.book_details.domain.model.BookDetails
import ru.mamykin.foboreader.book_details.domain.usecase.GetBookDetails
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import javax.inject.Inject
import javax.inject.Named

internal class BookDetailsFeature @Inject constructor(
    actor: BookDetailsActor,
    reducer: BookDetailsReducer,
) : ComposeFeature<BookDetailsFeature.State, BookDetailsFeature.Intent, Nothing, BookDetailsFeature.Action>(
    State.Loading,
    actor,
    reducer,
) {
    init {
        sendIntent(Intent.LoadBookInfo)
    }

    internal class BookDetailsActor @Inject constructor(
        @Named("bookId") private val bookId: Long,
        private val router: Router,
        private val screenProvider: ScreenProvider,
        private val getBookDetails: GetBookDetails,
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.OpenBook -> {
                    router.navigateTo(screenProvider.readBookScreen(bookId))
                }
                is Intent.LoadBookInfo -> {
                    emit(Action.BookLoaded(getBookDetails.execute(bookId)))
                }
            }
        }
    }

    internal class BookDetailsReducer @Inject constructor() : Reducer<State, Action, Nothing> {

        override fun invoke(state: State, action: Action): ReducerResult<State, Nothing> = when (action) {
            is Action.BookLoaded -> State.Loaded(
                bookDetails = action.bookDetails,
            ) to emptySet()
        }
    }

    sealed class Intent {
        object LoadBookInfo : Intent()
        object OpenBook : Intent()
    }

    sealed class Action {
        data class BookLoaded(val bookDetails: BookDetails) : Action()
    }

    sealed class State {
        object Loading : State()
        data class Loaded(
            val bookDetails: BookDetails,
        ) : State()
    }
}