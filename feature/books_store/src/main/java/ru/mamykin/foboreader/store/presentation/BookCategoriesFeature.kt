package ru.mamykin.foboreader.store.presentation

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.store.domain.usecase.GetBookCategories
import ru.mamykin.foboreader.store.navigation.BooksListScreen
import javax.inject.Inject

internal class BookCategoriesFeature @Inject constructor(
    reducer: BookCategoriesReducer,
    actor: BookCategoriesActor,
    private val uiEventTransformer: UiEventTransformer
) : Feature<BookCategories.ViewState, BookCategories.Intent, BookCategories.Effect, BookCategories.Action, Nothing>(
    BookCategories.ViewState(),
    actor,
    reducer
) {
    init {
        sendIntent(BookCategories.Intent.LoadCategories)
    }

    fun sendEvent(event: BookCategories.Event) {
        sendIntent(uiEventTransformer.invoke(event))
    }

    internal class BookCategoriesActor @Inject constructor(
        private val getBookCategories: GetBookCategories,
        private val router: Router,
    ) : Actor<BookCategories.Intent, BookCategories.Action> {

        override operator fun invoke(intent: BookCategories.Intent): Flow<BookCategories.Action> = flow {
            when (intent) {
                is BookCategories.Intent.LoadCategories -> {
                    emit(BookCategories.Action.Loading)
                    getBookCategories.execute().catchMap(
                        { emit(BookCategories.Action.LoadingSuccess(it)) },
                        { emit(BookCategories.Action.LoadingError(it.message.orEmpty())) }
                    )
                }
                is BookCategories.Intent.OpenCategory -> {
                    router.navigateTo(BooksListScreen(intent.id))
                }
            }
        }
    }

    internal class BookCategoriesReducer @Inject constructor() :
        Reducer<BookCategories.ViewState, BookCategories.Action, BookCategories.Effect> {

        override operator fun invoke(state: BookCategories.ViewState, action: BookCategories.Action) = when (action) {
            is BookCategories.Action.Loading -> {
                state.copy(
                    isLoading = true,
                    isError = false,
                    categories = emptyList()
                ) to emptySet()
            }
            is BookCategories.Action.LoadingSuccess -> {
                state.copy(
                    isLoading = false,
                    isError = false,
                    categories = action.categories
                ) to emptySet()
            }
            is BookCategories.Action.LoadingError -> {
                state.copy(
                    isLoading = false,
                    isError = false,
                    categories = emptyList()
                ) to setOf(
                    BookCategories.Effect.ShowSnackbar(action.message)
                )
            }
        }
    }

    internal class UiEventTransformer @Inject constructor() {

        operator fun invoke(event: BookCategories.Event): BookCategories.Intent = when (event) {
            is BookCategories.Event.RetryLoadClicked -> {
                BookCategories.Intent.LoadCategories
            }
            is BookCategories.Event.CategoryClicked -> {
                BookCategories.Intent.OpenCategory(event.id)
            }
        }
    }
}