package ru.mamykin.foboreader.read_book.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.Feature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.di.ReadBookScope
import ru.mamykin.foboreader.read_book.domain.model.Translation
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookContent
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookInfo
import ru.mamykin.foboreader.read_book.domain.usecase.GetParagraphTranslation
import ru.mamykin.foboreader.read_book.domain.usecase.GetWordTranslation
import javax.inject.Inject
import javax.inject.Named

@ReadBookScope
internal class ReadBookFeature @Inject constructor(
    actor: ReadBookActor,
    reducer: ReadBookReducer,
    private val uiEventTransformer: UiEventTransformer,
) : Feature<ReadBookFeature.State, ReadBookFeature.Intent, ReadBookFeature.Effect, ReadBookFeature.Action>(
    State(),
    actor,
    reducer,
) {
    init {
        sendIntent(Intent.LoadBookInfo)
    }

    fun sendEvent(event: Event) {
        sendIntent(uiEventTransformer.invoke(event))
    }

    internal class UiEventTransformer @Inject constructor() {

        operator fun invoke(event: Event): Intent = when (event) {
            is Event.TranslateParagraphClicked -> Intent.TranslateParagraph(event.paragraph)
            is Event.TranslateWordClicked -> Intent.TranslateWord(event.word)
            is Event.HideParagraphTranslationClicked -> Intent.HideParagraphTranslation
            is Event.HideWordTranslationClicked -> Intent.HideWordTranslation
        }
    }

    internal class ReadBookActor @Inject constructor(
        @Named("bookId") private val bookId: Long,
        private val getBookContent: GetBookContent,
        private val getParagraphTranslation: GetParagraphTranslation,
        private val getWordTranslation: GetWordTranslation,
        private val getBookInfo: GetBookInfo,
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.TranslateParagraph -> {
                    val paragraph = intent.paragraph
                    getParagraphTranslation.execute(paragraph)
                        ?.let { emit(Action.ParagraphTranslationLoaded(paragraph, it)) }
                        ?: emit(Action.ParagraphTranslationLoadingFailed)
                }
                is Intent.TranslateWord -> {
                    emit(Action.TranslationLoadingStarted)
                    getWordTranslation.execute(intent.word).fold(
                        { emit(Action.WordTranslationLoaded(it)) },
                        { emit(Action.WordTranslationLoadingFailed) }
                    )
                }
                is Intent.HideParagraphTranslation -> emit(Action.ParagraphTranslationHidden)
                is Intent.HideWordTranslation -> emit(Action.WordTranslationHidden)
                is Intent.LoadBookInfo -> {
                    emit(Action.BookLoadingStarted)
                    val bookInfo = getBookInfo.execute(bookId)
                    val bookContent = getBookContent.execute(bookInfo.filePath)
                    emit(Action.BookLoaded(bookInfo, bookContent.text))
                }
            }
        }
    }

    internal class ReadBookReducer @Inject constructor() : Reducer<State, Action, Effect> {

        override fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.BookLoadingStarted -> state.copy(isBookLoading = true) to emptySet()
            is Action.BookLoaded -> state.copy(
                isBookLoading = false,
                text = action.text,
                title = action.info.title,
                currentPage = action.info.currentPage
            ) to emptySet()
            is Action.TranslationLoadingStarted -> state.copy(isTranslationLoading = true) to emptySet()
            is Action.ParagraphTranslationLoaded -> state.copy(
                isTranslationLoading = false,
                paragraphTranslation = action.source to action.translation
            ) to emptySet()
            is Action.ParagraphTranslationLoadingFailed -> state to setOf(
                Effect.ShowSnackbar(R.string.read_book_translation_download_error)
            )
            is Action.ParagraphTranslationHidden -> state.copy(paragraphTranslation = null) to emptySet()
            is Action.WordTranslationLoaded -> state.copy(
                isTranslationLoading = false,
                wordTranslation = action.translation
            ) to emptySet()
            is Action.WordTranslationLoadingFailed -> state to setOf(
                Effect.ShowSnackbar(R.string.read_book_translation_download_error)
            )
            is Action.WordTranslationHidden -> state.copy(wordTranslation = null) to emptySet()
        }
    }

    sealed class Event {
        class TranslateParagraphClicked(val paragraph: String) : Event()
        class TranslateWordClicked(val word: String) : Event()
        object HideParagraphTranslationClicked : Event()
        object HideWordTranslationClicked : Event()
    }

    sealed class Intent {
        object LoadBookInfo : Intent()
        class TranslateParagraph(val paragraph: String) : Intent()
        class TranslateWord(val word: String) : Intent()
        object HideParagraphTranslation : Intent()
        object HideWordTranslation : Intent()
    }

    sealed class Effect {
        class ShowSnackbar(val messageId: Int) : Effect()
    }

    sealed class Action {
        object TranslationLoadingStarted : Action()

        class ParagraphTranslationLoaded(
            val source: String,
            val translation: String,
        ) : Action()

        object ParagraphTranslationLoadingFailed : Action()

        object ParagraphTranslationHidden : Action()

        class WordTranslationLoaded(
            val translation: Translation
        ) : Action()

        object WordTranslationLoadingFailed : Action()

        object WordTranslationHidden : Action()

        object BookLoadingStarted : Action()
        class BookLoaded(
            val info: BookInfo,
            val text: String,
        ) : Action()
    }

    data class State(
        val isBookLoading: Boolean = true,
        val isTranslationLoading: Boolean = false,
        val title: String = "",
        val text: String = "",
        val currentPage: Int = 0,
        val totalPages: Int = 0,
        val readPercent: Float = 0f,
        val wordTranslation: Translation? = null,
        val paragraphTranslation: Pair<String, String>? = null
    )
}