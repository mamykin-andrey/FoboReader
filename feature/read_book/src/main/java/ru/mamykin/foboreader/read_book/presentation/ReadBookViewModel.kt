package ru.mamykin.foboreader.read_book.presentation

import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.mvvm.BaseViewModel
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.domain.interactor.ReadBookInteractor

class ReadBookViewModel constructor(
    private val bookId: Long,
    private val interactor: ReadBookInteractor
) : BaseViewModel<ViewState, Action, Event, Effect>(
    ViewState(isBookLoading = true)
) {
    override fun loadData() {
        loadBookInfo()
    }

    private fun loadBookInfo() = launch {
        sendAction(Action.BookLoading)
        val info = interactor.getBookInfo(bookId)
        val content = interactor.getBookContent(info.filePath)
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

    override suspend fun onEvent(event: Event) {
        when (event) {
            is Event.TranslateParagraph -> translateParagraph(event.paragraph)
            is Event.HideParagraphTranslation -> hideParagraphTranslation()
            is Event.TranslateWord -> translateWord(event.word)
            is Event.PageOpened -> interactor.saveCurrentPage(bookId, event.pageNumber)
        }
    }

    private fun translateParagraph(paragraph: String) = launch {
        sendAction(Action.TranslationLoading)
        interactor.getParagraphTranslation(paragraph)
            ?.let { Action.ParagraphTranslationLoaded(paragraph, it) }
            ?.let { sendAction(it) }
            ?: sendEffect(Effect.ShowSnackbar(R.string.read_book_translation_download_error))
    }

    private fun hideParagraphTranslation() = launch {
        sendAction(Action.ParagraphTranslationHided)
    }

    private fun translateWord(word: String) = launch {
        sendAction(Action.TranslationLoading)

        interactor.getWordTranslation(word)
            ?.let { sendAction(Action.WordTranslationLoaded(word, it)) }
            ?: sendEffect(Effect.ShowSnackbar(R.string.read_book_translation_download_error))
    }
}