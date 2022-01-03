package ru.mamykin.foboreader.store.presentation

import ru.mamykin.foboreader.store.domain.model.BookCategory

object BookCategories {

    sealed class Event {
        object RetryLoadClicked : Event()
        class CategoryClicked(val id: String) : Event()
    }

    sealed class Intent {
        object LoadCategories : Intent()
        class OpenCategory(val id: String) : Intent()
    }

    sealed class Action {
        object Loading : Action()
        class LoadingSuccess(val categories: List<BookCategory>) : Action()
        class LoadingError(val message: String) : Action()
    }

    sealed class Effect {
        class ShowSnackbar(val message: String) : Effect()
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val isError: Boolean = false,
        val categories: List<BookCategory>? = null
    )
}