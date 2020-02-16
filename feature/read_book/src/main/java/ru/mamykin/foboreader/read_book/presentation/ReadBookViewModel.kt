package ru.mamykin.foboreader.read_book.presentation

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.read_book.data.model.FictionBook
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.read_book.domain.ReadBookInteractor

class ReadBookViewModel constructor(
        private val interactor: ReadBookInteractor
) : BaseViewModel<ReadBookViewModel.ViewState, ReadBookViewModel.Action>(
        initialState = ViewState(isLoading = true)
) {
    override fun loadData() {
        loadBookInfo()
    }

    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.TranslationLoading -> state.copy(
                isTranslationLoading = true
        )
        is Action.TranslationError -> state.copy(
                isTranslationLoading = false,
                isTranslationError = true
        )
        is Action.ParagraphTranslationLoaded -> state.copy(
                isTranslationLoading = false,
                paragraphTranslation = action.source to action.translation
        )
        is Action.ParagraphTranslationHided -> state.copy(
                paragraphTranslation = null
        )
        is Action.WordTranslationLoaded -> ViewState(
                isTranslationLoading = false,
                wordTranslation = action.source to action.translation
        )
        is Action.WordTranslationHided -> ViewState(
                wordTranslation = null
        )
        is Action.BookInfoLoading -> ViewState(
                isLoading = true
        )
        is Action.BookInfoLoaded -> state.copy(
                isLoading = false,
                bookInfo = action.book
        )
        is Action.BookLoadingError -> state.copy(
                isLoading = false,
                isBookLoadingError = true
        )
    }

    fun onParagraphClicked(paragraph: String) = launch {
        sendAction(Action.TranslationLoading)
        runCatching { interactor.getParagraphTranslation(paragraph) }
                .onSuccess { sendAction(Action.ParagraphTranslationLoaded(paragraph, it)) }
                .onFailure { sendAction(Action.TranslationError) }
    }

    fun onWordClicked(word: String) = launch {
        sendAction(Action.TranslationLoading)
        runCatching { interactor.getWordTranslation(word) }
                .onSuccess { sendAction(Action.WordTranslationLoaded(word, it)) }
                .onFailure { sendAction(Action.TranslationError) }
    }

    private fun loadBookInfo() = launch {
        sendAction(Action.BookInfoLoading)
        runCatching { interactor.getBookInfo() }
                .onSuccess { sendAction(Action.BookInfoLoaded(it)) }
                .onFailure { sendAction(Action.BookLoadingError) }
    }

    sealed class Action {
        object TranslationLoading : Action()
        data class ParagraphTranslationLoaded(val source: String, val translation: String) : Action()
        object ParagraphTranslationHided : Action()
        data class WordTranslationLoaded(val source: String, val translation: String) : Action()
        object WordTranslationHided : Action()
        object BookInfoLoading : Action()
        data class BookInfoLoaded(val book: FictionBook) : Action()
        object TranslationError : Action()
        object BookLoadingError : Action()
    }

    data class ViewState(
            val isLoading: Boolean = false,
            val isTranslationLoading: Boolean = false,
            val wordTranslation: Pair<String, String>? = null,
            val paragraphTranslation: Pair<String, String>? = null,
            val bookInfo: FictionBook? = null,
            val isBookLoadingError: Boolean = false,
            val isTranslationError: Boolean = false
    )
}