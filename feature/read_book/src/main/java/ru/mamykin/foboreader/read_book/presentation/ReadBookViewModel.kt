package ru.mamykin.foboreader.read_book.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookContent
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookInfo
import ru.mamykin.foboreader.read_book.domain.usecase.GetParagraphTranslation
import ru.mamykin.foboreader.read_book.domain.usecase.GetWordTranslation
import javax.inject.Inject
import javax.inject.Named

class ReadBookViewModel @Inject constructor(
    @Named("bookId") private val bookId: Long,
    private val getBookContent: GetBookContent,
    private val getParagraphTranslation: GetParagraphTranslation,
    private val getWordTranslation: GetWordTranslation,
    private val getBookInfo: GetBookInfo
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isBookLoading = true)
) {
    init {
        loadBookInfo()
    }

    private fun loadBookInfo() = viewModelScope.launch {
        sendAction(Action.BookLoading)
        val info = getBookInfo.execute(bookId).getOrThrow()
        val content = getBookContent.execute(info.filePath).getOrThrow()
        sendAction(Action.BookLoaded(info, content.text))
    }

    override fun onAction(action: Action): ViewState = when (action) {
        is Action.BookLoading -> state.copy(isBookLoading = true)
        is Action.BookLoaded -> state.copy(
            isBookLoading = false,
            text = action.text,
            title = action.info.title,
            currentPage = action.info.currentPage
        )
        is Action.TranslationLoading -> state.copy(isTranslationLoading = true)
        is Action.ParagraphTranslationLoaded -> state.copy(
            isTranslationLoading = false,
            paragraphTranslation = action.source to action.translation
        )
        is Action.ParagraphTranslationHided -> state.copy(paragraphTranslation = null)
        is Action.WordTranslationLoaded -> state.copy(
            isTranslationLoading = false,
            wordTranslation = action.translation
        )
        is Action.WordTranslationHided -> state.copy(wordTranslation = null)
    }

    override fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.TranslateParagraph -> translateParagraph(event.paragraph)
                is Event.TranslateWord -> translateWord(event.word)
                is Event.HideParagraphTranslation -> sendAction(Action.ParagraphTranslationHided)
                is Event.HideWordTranslation -> sendAction(Action.WordTranslationHided)
            }
        }
    }

    private fun translateParagraph(paragraph: String) {
        getParagraphTranslation.execute(paragraph)
            .doOnSuccess { it?.let { sendAction(Action.ParagraphTranslationLoaded(paragraph, it)) } }
            .doOnError { sendEffect(Effect.ShowSnackbar(R.string.read_book_translation_download_error)) }
    }

    private fun translateWord(word: String) = viewModelScope.launch {
        sendAction(Action.TranslationLoading)
        getWordTranslation.execute(word)
            .doOnSuccess { sendAction(Action.WordTranslationLoaded(it)) }
            .doOnError { sendEffect(Effect.ShowSnackbar(R.string.read_book_translation_download_error)) }
    }
}