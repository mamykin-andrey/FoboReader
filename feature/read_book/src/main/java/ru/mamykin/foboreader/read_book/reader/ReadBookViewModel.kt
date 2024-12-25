package ru.mamykin.foboreader.read_book.reader

import androidx.compose.ui.text.TextMeasurer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.translation.GetParagraphTranslation
import ru.mamykin.foboreader.read_book.translation.GetWordTranslation
import ru.mamykin.foboreader.read_book.translation.TextTranslation
import javax.inject.Inject
import javax.inject.Named

@ReadBookScope
internal class ReadBookViewModel @Inject constructor(
    @Named("bookId") private val bookId: Long,
    private val getBookText: GetBookText,
    private val getParagraphTranslation: GetParagraphTranslation,
    private val getWordTranslation: GetWordTranslation,
    private val getBookInfo: GetBookInfo,
    private val appSettingsRepository: AppSettingsRepository,
) : ViewModel() {

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.LoadBook -> {
                val info = getBookInfo.execute(bookId)
                val pages = getBookText.execute(info.filePath, intent.measurer, intent.screenSize)
                state = State.Content(
                    pages = pages,
                    title = info.title,
                    currentPage = info.currentPage,
                    textSize = appSettingsRepository.getReadTextSize().toFloat(),
                    readPercent = 0f,
                    totalPages = pages.size,
                )
            }

            is Intent.TranslateParagraph -> {
                getParagraphTranslation.execute(intent.paragraph)
                    ?.let {
                        val prevState = (state as? State.Content) ?: return@launch
                        state = prevState.copy(
                            paragraphTranslation = TextTranslation(
                                intent.paragraph,
                                listOf(it)
                            )
                        )
                        effectChannel.send(Effect.Vibrate)
                    } ?: run {
                    effectChannel.send(Effect.ShowSnackbar(R.string.read_book_translation_download_error))
                    effectChannel.send(Effect.Vibrate)
                }
            }

            is Intent.TranslateWord -> {
                getWordTranslation.execute(intent.word).fold(
                    {
                        val prevState = (state as? State.Content) ?: return@launch
                        state = prevState.copy(
                            wordTranslation = it
                        )
                        effectChannel.send(Effect.Vibrate)
                    },
                    {
                        effectChannel.send(Effect.ShowSnackbar(R.string.read_book_translation_download_error))
                        effectChannel.send(Effect.Vibrate)
                    }
                )
            }

            is Intent.HideParagraphTranslation -> {
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(paragraphTranslation = null)
                effectChannel.send(Effect.Vibrate)
            }

            is Intent.HideWordTranslation -> {
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(wordTranslation = null)
                effectChannel.send(Effect.Vibrate)
            }
        }
    }

    sealed class Intent {
        data class LoadBook(
            val measurer: TextMeasurer, val screenSize: Pair<Int, Int>
        ) : Intent()

        class TranslateParagraph(val paragraph: String) : Intent()
        class TranslateWord(val word: String) : Intent()
        data object HideParagraphTranslation : Intent()
        data object HideWordTranslation : Intent()
    }

    sealed class Effect {
        class ShowSnackbar(val messageId: Int) : Effect()
        data object Vibrate : Effect()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val title: String,
            val pages: List<String>,
            val textSize: Float?,
            val currentPage: Int,
            val totalPages: Int,
            val readPercent: Float,
            val wordTranslation: TextTranslation? = null,
            val paragraphTranslation: TextTranslation? = null,
        ) : State()
    }
}