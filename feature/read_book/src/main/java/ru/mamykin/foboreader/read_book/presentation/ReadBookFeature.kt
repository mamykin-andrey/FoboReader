package ru.mamykin.foboreader.read_book.presentation

import android.graphics.Color
import android.text.SpannableString
import androidx.annotation.ColorInt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.extension.setColor
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.core.presentation.ComposeFeature
import ru.mamykin.foboreader.core.presentation.Reducer
import ru.mamykin.foboreader.core.presentation.ReducerResult
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.di.ReadBookScope
import ru.mamykin.foboreader.read_book.domain.model.TextTranslation
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookInfo
import ru.mamykin.foboreader.read_book.domain.usecase.GetBookText
import ru.mamykin.foboreader.read_book.domain.usecase.GetParagraphTranslation
import ru.mamykin.foboreader.read_book.domain.usecase.GetWordTranslation
import javax.inject.Inject
import javax.inject.Named

@ReadBookScope
internal class ReadBookFeature @Inject constructor(
    actor: ReadBookActor,
    reducer: ReadBookReducer,
) : ComposeFeature<ReadBookFeature.State, ReadBookFeature.Intent, ReadBookFeature.Effect, ReadBookFeature.Action>(
    State.Loading,
    actor,
    reducer,
) {
    init {
        sendIntent(Intent.LoadBookInfo)
    }

    internal class ReadBookActor @Inject constructor(
        @Named("bookId") private val bookId: Long,
        private val getBookText: GetBookText,
        private val getParagraphTranslation: GetParagraphTranslation,
        private val getWordTranslation: GetWordTranslation,
        private val getBookInfo: GetBookInfo,
        private val appSettingsRepository: AppSettingsRepository,
    ) : Actor<Intent, Action> {

        override fun invoke(intent: Intent): Flow<Action> = flow {
            when (intent) {
                is Intent.TranslateParagraph -> {
                    getParagraphTranslation.execute(intent.paragraph)
                        ?.let { emit(Action.ParagraphTranslationLoaded(intent.paragraph, it)) }
                        ?: emit(Action.ParagraphTranslationFailed)
                }

                is Intent.TranslateWord -> {
                    getWordTranslation.execute(intent.word).fold(
                        { emit(Action.WordTranslationLoaded(it)) },
                        { emit(Action.WordTranslationFailed) }
                    )
                }

                is Intent.HideParagraphTranslation -> {
                    emit(Action.ParagraphTranslationHidden)
                }

                is Intent.HideWordTranslation -> {
                    emit(Action.WordTranslationHidden)
                }

                is Intent.LoadBookInfo -> {
                    val bookInfo = getBookInfo.execute(bookId)
                    val bookText = getBookText.execute(bookInfo.filePath)
                    emit(
                        Action.BookLoaded(
                            info = bookInfo,
                            text = bookText,
                            textSize = appSettingsRepository.getReadTextSize().toFloat(),
                        )
                    )
                }
            }
        }
    }

    internal class ReadBookReducer @Inject constructor(
        appSettingsRepository: AppSettingsRepository,
    ) : Reducer<State, Action, Effect> {

        @ColorInt
        private val translationColor = Color.parseColor(appSettingsRepository.getTranslationColor())

        override fun invoke(state: State, action: Action): ReducerResult<State, Effect> = when (action) {
            is Action.BookLoaded -> State.Content(
                text = action.text,
                title = action.info.title,
                currentPage = action.info.currentPage,
                textSize = action.textSize,
                readPercent = 0f,
                totalPages = 0,
            ) to emptySet()

            is Action.ParagraphTranslationLoaded -> (state as State.Content).copy(
                paragraphTranslation = getParagraphTranslationText(
                    action.paragraph,
                    action.translatedParagraph
                )
            ) to setOf(Effect.Vibrate)

            is Action.ParagraphTranslationFailed -> state to setOf(
                Effect.ShowSnackbar(R.string.read_book_translation_download_error),
                Effect.Vibrate,
            )

            is Action.ParagraphTranslationHidden -> (state as State.Content).copy(
                paragraphTranslation = null
            ) to setOf(Effect.Vibrate)

            is Action.WordTranslationLoaded -> (state as State.Content).copy(
                wordTranslation = action.translation
            ) to setOf(Effect.Vibrate)

            is Action.WordTranslationFailed -> state to setOf(
                Effect.ShowSnackbar(R.string.read_book_translation_download_error),
                Effect.Vibrate,
            )

            is Action.WordTranslationHidden -> (state as State.Content).copy(
                wordTranslation = null
            ) to emptySet()
        }

        private fun getParagraphTranslationText(
            paragraph: String,
            translatedParagraph: String
        ): CharSequence = SpannableString(paragraph + "\n\n" + translatedParagraph).apply {
            setColor(translationColor, end = length - 1)
        }
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
        class ParagraphTranslationLoaded(
            val paragraph: String,
            val translatedParagraph: String,
        ) : Action()

        object ParagraphTranslationFailed : Action()

        object ParagraphTranslationHidden : Action()

        class WordTranslationLoaded(
            val translation: TextTranslation
        ) : Action()

        object WordTranslationFailed : Action()

        object WordTranslationHidden : Action()

        class BookLoaded(
            val info: BookInfo,
            val text: String,
            val textSize: Float?,
        ) : Action()
    }

    sealed class State {
        object Loading : State()
        data class Content(
            val title: String,
            val text: String,
            val textSize: Float?,
            val currentPage: Int,
            val totalPages: Int,
            val readPercent: Float,
            val wordTranslation: TextTranslation? = null,
            val paragraphTranslation: CharSequence? = null,
        ) : State()
    }
}