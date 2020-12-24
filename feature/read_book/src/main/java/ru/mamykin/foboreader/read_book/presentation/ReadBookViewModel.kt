package ru.mamykin.foboreader.read_book.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.BaseViewModel
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookContent
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookInfo
import ru.mamykin.foboreader.read_book.domain.usecase.GetParagraphTranslation
import ru.mamykin.foboreader.read_book.domain.usecase.UpdateBookInfo

class ReadBookViewModel(
    private val bookId: Long,
    private val getBookContent: GetBookContent,
    private val getParagraphTranslation: GetParagraphTranslation,
    private val getBookInfo: GetBookInfo,
    private val updateBookInfo: UpdateBookInfo
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isBookLoading = true)
) {
    override fun loadData() {
        loadBookInfo()
    }

    private fun loadBookInfo() = viewModelScope.launch {
        sendAction(Action.BookLoading)
        val info = getBookInfo.execute(bookId)
        val content = getBookContent.execute(info.filePath)
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
            wordTranslation = action.source to action.translation
        )
        is Action.WordTranslationHided -> state.copy(wordTranslation = null)
    }

    override fun onEvent(event: Event) {
        viewModelScope.launch {
            when (event) {
                is Event.TranslateParagraph -> translateParagraph(event.paragraph)
                is Event.HideParagraphTranslation -> sendAction(Action.ParagraphTranslationHided)
                is Event.PageLoaded -> updateBookInfo.execute(bookId, event.currentPage, event.totalPages)
            }
        }
    }

    private fun translateParagraph(paragraph: String) {
        sendAction(Action.TranslationLoading)
        getParagraphTranslation.execute(paragraph)
            ?.let { Action.ParagraphTranslationLoaded(paragraph, it) }
            ?.let { sendAction(it) }
            ?: sendEffect(Effect.ShowSnackbar(R.string.read_book_translation_download_error))
    }
}