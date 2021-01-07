package ru.mamykin.foboreader.read_book.presentation

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.read_book.domain.entity.TranslationEntity

sealed class Event {
    data class TranslateParagraph(val paragraph: String) : Event()
    data class TranslateWord(val word: String) : Event()
    object HideParagraphTranslation : Event()
    object HideWordTranslation : Event()
}

sealed class Effect {
    data class ShowSnackbar(val messageId: Int) : Effect()
}

sealed class Action {
    object TranslationLoading : Action()

    data class ParagraphTranslationLoaded(
        val source: String,
        val translation: String
    ) : Action()

    object ParagraphTranslationHided : Action()

    data class WordTranslationLoaded(
        val translation: TranslationEntity
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
    val wordTranslation: TranslationEntity? = null,
    val paragraphTranslation: Pair<String, String>? = null
)