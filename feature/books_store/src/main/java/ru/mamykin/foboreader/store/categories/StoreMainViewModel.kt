package ru.mamykin.foboreader.store.categories

import dagger.hilt.android.lifecycle.HiltViewModel
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.core.presentation.StringOrResource
import javax.inject.Inject

@HiltViewModel
internal class StoreMainViewModel @Inject constructor(
    private val getBookCategories: GetBookCategoriesUseCase,
    private val errorMessageMapper: ErrorMessageMapper,
) : BaseViewModel<StoreMainViewModel.Intent, StoreMainViewModel.State, StoreMainViewModel.Effect>(
    State.Loading
) {
    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.LoadCategories -> {
                println("LoadCategories")
                if (state is State.Content) return

                println("Before loading")
                state = State.Loading
                println("After loading")
                val result = getBookCategories.execute()
                if (result.isSuccess) {
                    println("Success fold")
                    runCatching {
                        val list = result.getOrThrow()
                        state = State.Content(list.map { BookCategoryUIModel.fromDomainModel(it) })
                    }.getOrElse {
                        println(it)
                    }
                    println("After success fold")
                } else {
                    println("Error fold")
                    state = State.Error(errorMessageMapper.getMessage(result.exceptionOrNull()!!))
                }
            }

            is Intent.ReloadCategories -> {
                state = State.Loading
                handleIntent(Intent.ReloadCategories)
            }

            is Intent.OpenCategory -> {
                sendEffect(Effect.OpenBooksListScreen(intent.categoryId))
            }
        }
    }

    sealed class Intent {
        data object LoadCategories : Intent()
        data object ReloadCategories : Intent()
        class OpenCategory(val categoryId: String) : Intent()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()
        class OpenBooksListScreen(val categoryId: String) : Effect()
    }

    sealed class State {
        data object Loading : State()

        data class Content(
            val categories: List<BookCategoryUIModel>,
        ) : State()

        data class Error(
            val message: StringOrResource
        ) : State()
    }
}