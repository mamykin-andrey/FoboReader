package ru.mamykin.foboreader.read_book.presentation

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.domain.model.BookInfo
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.read_book.domain.ReadBookInteractor

class ReadBookViewModel constructor(
        private val interactor: ReadBookInteractor
) : BaseViewModel<ReadBookViewModel.ViewState, ReadBookViewModel.Action>(
        ViewState(isBookLoading = true)
) {
    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BookLoading -> state.copy(isBookLoading = true)
        is Action.BookLoaded -> state.copy(
                isBookLoading = false,
                text = action.text,
                title = action.info.title,
                currentPage = action.info.currentPage
        )
        is Action.TranslationLoading -> state.copy(isTranslationLoading = true)
        is Action.TranslationError -> state.copy(
                isTranslationLoading = false,
                error = "Не удалось загрузить перевод"
        )
        is Action.ParagraphTranslationLoaded -> state.copy(
                isTranslationLoading = false,
                paragraphTranslation = action.source to action.translation
        )
        is Action.ParagraphTranslationHided -> state.copy(paragraphTranslation = null)
        is Action.WordTranslationLoaded -> state.copy(
                isTranslationLoading = false,
                wordTranslation = action.source to action.translation
        )
        is Action.WordTranslationHided -> state.copy(wordTranslation = null)
    }

    fun loadBookInfo(id: Long) = launch {
        sendAction(Action.BookLoading)

        val info = interactor.getBookInfo(id)
        val content = interactor.getBookContent(info.filePath)

        sendAction(Action.BookLoaded(info, content.text))
    }

    fun onParagraphClicked(paragraph: String) = launch {
        if (state.paragraphTranslation != null) {
            sendAction(Action.ParagraphTranslationHided)
        } else {
            sendAction(Action.TranslationLoading)
            interactor.getParagraphTranslation(paragraph)
                    ?.let { sendAction(Action.ParagraphTranslationLoaded(paragraph, it)) }
                    ?: sendAction(Action.TranslationError)
        }
    }

    fun onWordClicked(word: String) = launch {
        sendAction(Action.TranslationLoading)

        interactor.getWordTranslation(word)
                ?.let { sendAction(Action.WordTranslationLoaded(word, it)) }
                ?: sendAction(Action.TranslationError)
    }

    sealed class Action {
        object TranslationLoading : Action()
        object TranslationError : Action()

        data class ParagraphTranslationLoaded(
                val source: String,
                val translation: String
        ) : Action()

        object ParagraphTranslationHided : Action()

        data class WordTranslationLoaded(
                val source: String,
                val translation: String
        ) : Action()

        object WordTranslationHided : Action()

        object BookLoading : Action()
        data class BookLoaded(val info: BookInfo, val text: String) : Action()
    }

    data class ViewState(
            val isBookLoading: Boolean = false,
            val isTranslationLoading: Boolean = false,
            val title: String = "",
            val text: String = "",
            val currentPage: Int = 0,
            val totalPages: Int = 0,
            val readPercent: Float = 0f,
            val wordTranslation: Pair<String, String>? = null,
            val paragraphTranslation: Pair<String, String>? = null,
            val error: String? = null
    )
}