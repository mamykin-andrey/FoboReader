package ru.mamykin.foboreader.read_book.reader

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.extension.foldCancellable
import ru.mamykin.foboreader.core.presentation.BaseViewModel
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
    getVibrationEnabled: GetVibrationEnabledUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ReadBookViewModel.Intent, ReadBookViewModel.State, ReadBookViewModel.Effect>(
    State.Loading(
        StringOrResource.Resource(R.string.rb_book_loading)
    )
) {
    private val bookId: Long = savedStateHandle.get<Long>("bookId")!!
    private var bookPages: List<Book.Page>? = null
    private var wordsDictionary: Map<String, String> = emptyMap()

    private val isVibrationEnabled = getVibrationEnabled.execute()
    private var loadBookJob: Job? = null

    override suspend fun handleIntent(intent: Intent) {
        when (intent) {
            is Intent.ReloadBook -> {
                state = State.Loading(StringOrResource.Resource(R.string.rb_book_loading))
            }

            is Intent.LoadBook -> {
                if (state is State.Content) return
                loadBookJob?.cancel()
                loadBookJob = null
                loadBook(intent)
            }

            is Intent.TranslateSentence -> {
                val prevState = (state as? State.Content) ?: return
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
                val prevState = (state as? State.Content) ?: return
                val translation = wordsDictionary[intent.word] ?: translateWordRemote(intent)
                if (translation != null) {
                    state = prevState.copy(
                        wordTranslation = TextTranslation(
                            intent.word,
                            listOf(translation),
                        )
                    )
                    vibrateIfEnabled()
                } else {
                    sendEffect(
                        Effect.ShowSnackbar(
                            SnackbarData(StringOrResource.Resource(R.string.read_book_translation_download_error))
                        )
                    )
                    vibrateIfEnabled()
                }
            }

            is Intent.HideParagraphTranslation -> {
                val prevState = (state as? State.Content) ?: return
                state = prevState.copy(paragraphTranslation = null)
                vibrateIfEnabled()
            }

            is Intent.HideWordTranslation -> {
                val prevState = (state as? State.Content) ?: return
                state = prevState.copy(wordTranslation = null)
                vibrateIfEnabled()
            }

            is Intent.PageChanged -> {
                val bookPages = bookPages ?: return
                val prevState = (state as? State.Content) ?: return
                updateBookInfoUseCase.execute(bookId, intent.pageIndex, bookPages.size)
                state = prevState.copy(
                    currentPage = intent.pageIndex,
                    totalPages = bookPages.size,
                    readPercent = calculateReadPercent(intent.pageIndex, bookPages.size),
                )
            }
        }
    }

    private suspend fun loadBook(intent: Intent.LoadBook) = coroutineScope {
        loadBookJob = launch {
            getBookUseCase.execute(
                bookId,
                ComposeTextMeasurer(intent.measurer),
                intent.screenSize,
            ).foldCancellable(
                onSuccess = { showBookContent(it, intent) },
                onFailure = { showOpenBookError() },
            )
        }
    }

    private fun showOpenBookError() {
        state = State.Failed(StringOrResource.Resource(ru.mamykin.foboreader.core.R.string.cr_error_title))
    }

    private fun showBookContent(
        book: Book,
        intent: Intent.LoadBook
    ) {
        bookPages = book.pages
        wordsDictionary = book.dictionary
        val userSettings = State.Content.UserSettings(
            fontSize = book.userSettings.fontSize,
            translationColorCode = book.userSettings.translationColorCode,
            backgroundColorCode = book.userSettings.backgroundColorCode,
        )
        state = State.Content(
            bookId = book.info.id,
            pages = book.pages.map { it.toTranslatedAnnotatedString() },
            title = StringOrResource.String(book.info.title),
            currentPage = book.info.currentPage,
            userSettings = userSettings,
            readPercent = calculateReadPercent(book.info.currentPage, book.pages.size),
            textHeight = intent.screenSize.first,
            textWidth = intent.screenSize.second,
            totalPages = book.pages.size,
        )
    }

    private suspend fun translateWordRemote(intent: Intent.TranslateWord): String? {
        return getWordTranslation.execute(intent.word).getOrNull()?.getMostPreciseTranslation()
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
                    tag = TextAnnotations.SENTENCE_NUMBER,
                    annotation = i.toString(),
                    start = sentenceStartInText,
                    end = length,
                )
                val wordsPositions = getWordsPositions(sentence)
                wordsPositions.forEach { (wordInSentenceStart, wordInSentenceEnd) ->
                    addStringAnnotation(
                        tag = TextAnnotations.WORD,
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
            sendEffect(Effect.Vibrate)
        }
    }

    sealed class Intent {
        data object ReloadBook : Intent()

        data class LoadBook(
            val measurer: TextMeasurer,
            val screenSize: Pair<Int, Int>,
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

    sealed class State(
        open val title: StringOrResource
    ) {
        data class Loading(
            override val title: StringOrResource,
        ) : State(title)

        data class Content(
            override val title: StringOrResource,
            val bookId: Long,
            val pages: List<AnnotatedString>,
            val textHeight: Int,
            val textWidth: Int,
            val userSettings: UserSettings,
            val currentPage: Int,
            val totalPages: Int,
            val readPercent: Float,
            val wordTranslation: TextTranslation? = null,
            val paragraphTranslation: TextTranslation? = null,
        ) : State(title) {
            data class UserSettings(
                val fontSize: Int,
                val translationColorCode: String,
                val backgroundColorCode: String,
            )
        }

        data class Failed(override val title: StringOrResource) : State(title)
    }
}