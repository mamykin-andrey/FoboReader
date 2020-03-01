package ru.mamykin.foboreader.read_book.presentation

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.domain.model.BookInfo
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.read_book.domain.ReadBookInteractor

class ReadBookViewModel constructor(
        private val interactor: ReadBookInteractor
) : BaseViewModel<ReadBookViewModel.ViewState, ReadBookViewModel.Action>(
        ViewState(isBookInfoLoading = true)
) {
    override fun reduceState(action: Action): ViewState = when (action) {
        is Action.BookInfoLoading -> state.copy(isBookInfoLoading = true)
        is Action.BookInfoLoaded -> state.copy(
                isBookInfoLoading = false,
                pageText = "Hello world",
                title = action.bookInfo.title,
                currentPage = action.bookInfo.currentPage
        )
        is Action.BookPageLoaded -> state.copy(
                pageText = action.pageText,
                currentPage = action.pageNum
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
        sendAction(Action.BookInfoLoading)

        val bookInfo = interactor.getBookInfo(id)
        sendAction(Action.BookInfoLoaded(bookInfo))

        val (text, num) = interactor.getBookText()
        sendAction(Action.BookPageLoaded(text, num))
    }

    fun onParagraphClicked(paragraph: String) = launch {
        sendAction(Action.TranslationLoading)

        interactor.getParagraphTranslation(paragraph)
                ?.let { sendAction(Action.ParagraphTranslationLoaded(paragraph, it)) }
                ?: sendAction(Action.TranslationError)
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

        object BookInfoLoading : Action()
        data class BookInfoLoaded(val bookInfo: BookInfo) : Action()

        data class BookPageLoaded(
                val pageText: String,
                val pageNum: Int
        ) : Action()
    }

    data class ViewState(
            val isBookInfoLoading: Boolean = false,
            val isTranslationLoading: Boolean = false,
            val title: String = "",
            val pageText: String = "",
            val currentPage: Int = 0,
            val totalPages: Int = 0,
            val readPercent: Float = 0f,
            val wordTranslation: Pair<String, String>? = null,
            val paragraphTranslation: Pair<String, String>? = null,
            val error: String? = null
    )
}