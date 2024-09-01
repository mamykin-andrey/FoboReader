package ru.mamykin.foboreader.store.categories

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.store.list.BooksListScreen
import javax.inject.Inject

@BookCategoriesScope
internal class BookCategoriesFeature @Inject constructor(
    reducer: BookCategoriesReducer,
    actor: BookCategoriesActor,
) : ComposeFeature<BookCategoriesFeature.State, BookCategoriesFeature.Intent, BookCategoriesFeature.Effect, BookCategoriesFeature.Action>(
    State.Progress,
    actor,
    reducer
) {
    init {
        sendIntent(Intent.LoadCategories)
    }

    internal class BookCategoriesActor @Inject constructor(
        private val getBookCategories: GetBookCategories,
        private val router: Router,
    ) : Actor<Intent, Action> {

        override operator fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.LoadCategories -> {
                    emit(Action.Loading)
                    getBookCategories.execute().fold(
                        { emit(Action.LoadingSuccess(it)) },
                        { emit(Action.LoadingError(it)) }
                    )
                }
                is Intent.OpenCategory -> {
                    router.navigateTo(BooksListScreen(intent.id))
                }
            }
        }
    }

    internal class BookCategoriesReducer @Inject constructor(
        private val errorMessageMapper: ErrorMessageMapper,
    ) : Reducer<State, Action, Effect> {

        override operator fun invoke(state: State, action: Action) = when (action) {
            is Action.Loading -> State.Progress to emptySet<Effect>()
            is Action.LoadingSuccess -> State.Content(action.categories) to emptySet()
            is Action.LoadingError -> State.Error(errorMessageMapper.getMessage(action.error)) to emptySet()
        }
    }

    sealed class Intent {
        object LoadCategories : Intent()
        class OpenCategory(val id: String) : Intent()
    }

    sealed class Action {
        object Loading : Action()
        class LoadingSuccess(val categories: List<BookCategory>) : Action()
        class LoadingError(val error: Throwable) : Action()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()
    }

    sealed class State {

        object Progress : State()

        data class Content(
            val categories: List<BookCategory>
        ) : State()

        data class Error(
            val errorMessage: String
        ) : State()
    }
}