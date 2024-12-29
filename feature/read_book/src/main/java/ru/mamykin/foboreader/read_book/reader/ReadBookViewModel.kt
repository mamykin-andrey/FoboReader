package ru.mamykin.foboreader.read_book.reader

import androidx.compose.ui.text.TextMeasurer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.core.presentation.SnackbarData
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.translation.GetParagraphTranslation
import ru.mamykin.foboreader.read_book.translation.GetWordTranslation
import ru.mamykin.foboreader.read_book.translation.TextTranslation
import javax.inject.Inject

@HiltViewModel
internal class ReadBookViewModel @Inject constructor(
    private val getParagraphTranslation: GetParagraphTranslation,
    private val getWordTranslation: GetWordTranslation,
    private val appSettingsRepository: AppSettingsRepository,
    private val getBookUseCase: GetBookUseCase,
    getVibrationEnabled: GetVibrationEnabled,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId: Long = savedStateHandle.get<Long>("bookId")!!
    private var book: Book? = null

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    private val isVibrationEnabled = getVibrationEnabled.execute()
    private var loadBookJob: Job? = null

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.LoadBook -> {
                loadBookJob?.cancel()
                loadBookJob = null
                coroutineScope {
                    loadBookJob = launch {
                        this@ReadBookViewModel.book = getBookUseCase.execute(
                            bookId,
                            ComposeTextPageSplitter(intent.measurer),
                            intent.screenSize,
                        )
                        val book = requireNotNull(this@ReadBookViewModel.book)
                        state = State.Content(
                            pages = book.pages,
                            title = book.info.title,
                            currentPage = book.info.currentPage,
                            fontSize = book.fontSize,
                            readPercent = 0f,
                            textHeight = intent.screenSize.first,
                            textWidth = intent.screenSize.second,
                            totalPages = book.pages.size,
                        )
                    }
                }
            }

            is Intent.TranslateParagraph -> {
                val translation = getParagraphTranslation.execute(
                    requireNotNull(this@ReadBookViewModel.book),
                    intent.paragraph
                )
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(
                    paragraphTranslation = TextTranslation(
                        sourceText = intent.paragraph,
                        textTranslations = listOf(translation)
                    )
                )
                vibrateIfEnabled()
            }

            is Intent.TranslateWord -> {
                getWordTranslation.execute(intent.word).fold(
                    {
                        val prevState = (state as? State.Content) ?: return@launch
                        state = prevState.copy(
                            wordTranslation = it
                        )
                        vibrateIfEnabled()
                    },
                    {
                        effectChannel.send(
                            Effect.ShowSnackbar(
                                SnackbarData(StringOrResource.Resource(R.string.read_book_translation_download_error))
                            )
                        )
                        vibrateIfEnabled()
                    }
                )
            }

            is Intent.HideParagraphTranslation -> {
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(paragraphTranslation = null)
                vibrateIfEnabled()
            }

            is Intent.HideWordTranslation -> {
                val prevState = (state as? State.Content) ?: return@launch
                state = prevState.copy(wordTranslation = null)
                vibrateIfEnabled()
            }
        }
    }

    private suspend fun vibrateIfEnabled() {
        if (isVibrationEnabled) {
            effectChannel.send(Effect.Vibrate)
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
        class ShowSnackbar(val data: SnackbarData) : Effect()
        data object Vibrate : Effect()
    }

    sealed class State {
        data object Loading : State()
        data class Content(
            val title: String,
            val pages: List<String>,
            val textHeight: Int,
            val textWidth: Int,
            val fontSize: Int,
            val currentPage: Int,
            val totalPages: Int,
            val readPercent: Float,
            val wordTranslation: TextTranslation? = null,
            val paragraphTranslation: TextTranslation? = null,
        ) : State()
    }
}