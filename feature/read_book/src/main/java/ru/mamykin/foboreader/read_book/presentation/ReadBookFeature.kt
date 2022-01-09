package ru.mamykin.foboreader.read_book.presentation

import android.graphics.Color
import android.text.SpannableString
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.extension.setColor
import ru.mamykin.foboreader.core.extension.trimSpecialCharacters
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
) : Feature<ReadBookFeature.State, ReadBookFeature.Intent, ReadBookFeature.Effect, ReadBookFeature.Action>(
    State(),
    actor,
    reducer,
) {
    init {
        sendIntent(Intent.LoadBookInfo)
    }

    internal class ReadBookActor @Inject constructor(
        @Named("bookId") private val bookId: Long,
        private val getBookContent: GetBookContent,
        private val getParagraphTranslation: GetParagraphTranslation,
        private val getWordTranslation: GetWordTranslation,
        private val getBookInfo: GetBookInfo,
        private val appSettingsStorage: AppSettingsStorage,
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.TranslateParagraph -> {
                    val paragraph = intent.paragraph.trim()
                    getParagraphTranslation.execute(paragraph)
                        ?.let { emit(Action.ParagraphTranslationLoaded(paragraph, it)) }
                        ?: emit(Action.ParagraphTranslationLoadingFailed)
                }

                is Intent.TranslateWord -> {
                    emit(Action.TranslationLoadingStarted)
                    val word = intent.word.trimSpecialCharacters()
                    getWordTranslation.execute(word).fold(
                        { emit(Action.WordTranslationLoaded(it)) },
                        { emit(Action.WordTranslationLoadingFailed) }
                    )
                }

                is Intent.HideParagraphTranslation -> {
                    emit(Action.ParagraphTranslationHidden)
                }

                is Intent.HideWordTranslation -> {
                    emit(Action.WordTranslationHidden)
                }

                is Intent.LoadBookInfo -> {
                    emit(Action.BookLoadingStarted)
                    val bookInfo = getBookInfo.execute(bookId)
                    val bookContent = getBookContent.execute(bookInfo.filePath)
                    emit(
                        Action.BookLoaded(
                            info = bookInfo,
                            text = bookContent.text,
                            textSize = appSettingsStorage.readTextSize.toFloat(),
                        )
                    )
                }
            }
        }
    }

    internal class ReadBookReducer @Inject constructor(
        private val appSettingsStorage: AppSettingsStorage,
    ) : Reducer<State, Action, Effect> {

        override fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.BookLoadingStarted -> state.copy(isBookLoading = true) to emptySet()

            is Action.BookLoaded -> state.copy(
                isBookLoading = false,
                text = action.text,
                title = action.info.title,
                currentPage = action.info.currentPage,
                textSize = action.textSize,
            ) to emptySet()

            is Action.TranslationLoadingStarted -> state.copy(isTranslationLoading = true) to emptySet()

            is Action.ParagraphTranslationLoaded -> state.copy(
                isTranslationLoading = false,
                paragraphTranslation = getParagraphTranslationText(action.paragraph, action.translatedParagraph)
            ) to setOf(
                Effect.Vibrate,
            )

            is Action.ParagraphTranslationLoadingFailed -> state to setOf(
                Effect.ShowSnackbar(R.string.read_book_translation_download_error),
                Effect.Vibrate,
            )
            is Action.ParagraphTranslationHidden -> state.copy(
                paragraphTranslation = null
            ) to setOf(Effect.Vibrate)

            is Action.WordTranslationLoaded -> state.copy(
                isTranslationLoading = false,
                wordTranslation = action.translation
            ) to setOf(Effect.Vibrate)

            is Action.WordTranslationLoadingFailed -> state to setOf(
                Effect.ShowSnackbar(R.string.read_book_translation_download_error),
                Effect.Vibrate,
            )

            is Action.WordTranslationHidden -> state.copy(wordTranslation = null) to emptySet()
        }

        private fun getParagraphTranslationText(paragraph: String, translatedParagraph: String): CharSequence {
            return SpannableString(paragraph + "\n\n" + translatedParagraph).apply {
                setColor(
                    color = Color.parseColor(appSettingsStorage.translationColor),
                    start = paragraph.length,
                    end = length - 1
                )
            }
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
        object Vibrate : Effect()
    }

    sealed class Action {
        object TranslationLoadingStarted : Action()

        class ParagraphTranslationLoaded(
            val paragraph: String,
            val translatedParagraph: String,
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
            val textSize: Float?,
        ) : Action()
    }

    data class State(
        val isBookLoading: Boolean = true,
        val isTranslationLoading: Boolean = false,
        val title: String = "",
        val text: String = "",
        val textSize: Float? = null,
        val currentPage: Int = 0,
        val totalPages: Int = 0,
        val readPercent: Float = 0f,
        val wordTranslation: Translation? = null,
        val paragraphTranslation: CharSequence? = null,
    )
}