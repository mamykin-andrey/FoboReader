package ru.mamykin.foboreader.store.presentation

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.store.di.BookCategoriesScope
import ru.mamykin.foboreader.store.domain.model.BookCategory
import ru.mamykin.foboreader.store.domain.usecase.GetBookCategories
import ru.mamykin.foboreader.store.navigation.BooksListScreen
import javax.inject.Inject

@BookCategoriesScope
internal class BookCategoriesFeature @Inject constructor(
    reducer: BookCategoriesReducer,
    actor: BookCategoriesActor,
) : Feature<BookCategoriesFeature.State, BookCategoriesFeature.Intent, BookCategoriesFeature.Effect, BookCategoriesFeature.Action>(
    State(),
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
            is Action.Loading -> {
                state.copy(
                    isLoading = true,
                    errorMessage = null,
                    categories = emptyList()
                ) to emptySet<Effect>()
            }
            is Action.LoadingSuccess -> {
                state.copy(
                    isLoading = false,
                    errorMessage = null,
                    categories = action.categories
                ) to emptySet()
            }
            is Action.LoadingError -> {
                state.copy(
                    isLoading = false,
                    errorMessage = errorMessageMapper.getMessage(action.error),
                    categories = emptyList()
                ) to emptySet()
            }
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

    // TODO: Use sealed class
    data class State(
        val isLoading: Boolean = true,
        val errorMessage: String? = null,
        val categories: List<BookCategory>? = null
    )
}