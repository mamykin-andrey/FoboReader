package ru.mamykin.foboreader.read_book.reader

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.presentation.LoggingEffectChannel
import ru.mamykin.foboreader.core.presentation.LoggingStateDelegate
import ru.mamykin.foboreader.core.presentation.SnackbarData
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.translation.GetWordTranslation
import ru.mamykin.foboreader.read_book.translation.TextTranslation
import javax.inject.Inject

@HiltViewModel
internal class ReadBookViewModel @Inject constructor(
    private val getWordTranslation: GetWordTranslation,
    private val getBookUseCase: GetBookUseCase,
    private val updateBookInfoUseCase: UpdateBookInfoUseCase,
    getVibrationEnabled: GetVibrationEnabled,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val bookId: Long = savedStateHandle.get<Long>("bookId")!!
    private var bookPages: List<Book.Page>? = null

    var state: State by LoggingStateDelegate(State.Loading)
        private set

    private val effectChannel = LoggingEffectChannel<Effect>()
    val effectFlow = effectChannel.receiveAsFlow()

    private val isVibrationEnabled = getVibrationEnabled.execute()
    private var loadBookJob: Job? = null
    private var isDataLoaded = false

    fun sendIntent(intent: Intent) = viewModelScope.launch {
        when (intent) {
            is Intent.LoadBook -> {
                if (isDataLoaded) return@launch
                loadBookJob?.cancel()
                loadBookJob = null
                coroutineScope {
                    loadBookJob = launch {
                        val book = getBookUseCase.execute(
                            bookId,
                            ComposeTextMeasurer(intent.measurer),
                            intent.screenSize,
                        )
                        bookPages = book.pages
                        isDataLoaded = true
                        state = State.Content(
                            pages = book.pages.map { it.toTranslatedAnnotatedString() },
                            title = book.info.title,
                            currentPage = book.info.currentPage,
                            fontSize = book.fontSize,
                            readPercent = calculateReadPercent(book.info.currentPage, book.pages.size),
                            textHeight = intent.screenSize.first,
                            textWidth = intent.screenSize.second,
                            totalPages = book.pages.size,
                        )
                    }
                }
            }

            is Intent.TranslateSentence -> {
                val prevState = (state as? State.Content) ?: return@launch
                val page = requireNotNull(bookPages)[prevState.currentPage]
                val sentence = page.sentences[intent.index]
                val translation = page.translations[intent.index]
                state = prevState.copy(
                    paragraphTranslation = TextTranslation(
                        sourceText = sentence,
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

            is Intent.PageChanged -> {
                val bookPages = bookPages ?: return@launch
                val prevState = (state as? State.Content) ?: return@launch
                updateBookInfoUseCase.execute(bookId, intent.pageIndex, bookPages.size)
                state = prevState.copy(
                    currentPage = intent.pageIndex,
                    totalPages = bookPages.size,
                    readPercent = calculateReadPercent(intent.pageIndex, bookPages.size),
                )
            }
        }
    }

    private fun calculateReadPercent(currentPageIndex: Int, totalPages: Int): Float {
        return (currentPageIndex + 1f) / totalPages.toFloat() * 100
    }

    private fun Book.Page.toTranslatedAnnotatedString(): AnnotatedString {
        val sentences = this.sentences
        return buildAnnotatedString {
            for (i in 0 until sentences.lastIndex) {
                val sentence = sentences[i] + "\n"
                append(sentence)
                val sentenceStartInText = length - sentence.length
                addStringAnnotation(
                    tag = TextAnnotation.SENTENCE_NUMBER,
                    annotation = i.toString(),
                    start = sentenceStartInText,
                    end = length,
                )
                val wordsPositions = getWordsPositions(sentence)
                wordsPositions.forEach { (wordInSentenceStart, wordInSentenceEnd) ->
                    addStringAnnotation(
                        tag = TextAnnotation.WORD,
                        annotation = sentence.substring(wordInSentenceStart, wordInSentenceEnd),
                        start = sentenceStartInText + wordInSentenceStart,
                        end = sentenceStartInText + wordInSentenceEnd,
                    )
                }
            }
        }
    }

    private fun getWordsPositions(text: String): List<Pair<Int, Int>> {
        val wordPositions = mutableListOf<Pair<Int, Int>>()
        var wordStartPos = 0
        for (i in text.indices) {
            val character = text[i]
            if (character.isLetter() || character == '\'') {
                if (wordStartPos == wordPositions.lastOrNull()?.first) {
                    wordStartPos = i
                }
            } else if (wordStartPos != wordPositions.lastOrNull()?.first) {
                wordPositions.add(wordStartPos to i)
            }
        }
        return wordPositions
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

        class TranslateSentence(val index: Int) : Intent()
        data class PageChanged(val pageIndex: Int) : Intent()
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
            val pages: List<AnnotatedString>,
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