package ru.mamykin.foboreader.read_book.presentation

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Px
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.di.DaggerReadBookComponent
import ru.mamykin.foboreader.read_book.domain.model.TextTranslation
import ru.mamykin.foboreader.read_book.platform.VibrationManager
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

// TODO: Optimize the Composable functions
// TODO: Remove the split/find words/paragraphs logic from UI
// TODO: Fix the loading on main thread issue
// TODO: Fix handing clicks by the text view when the popup is shown
// TODO: Add support of parapgraph translation pagination
class ReadBookFragment : BaseFragment() {

    companion object {

        private const val EXTRA_BOOK_ID = "extra_book_id"

        fun newInstance(bookId: Long) = ReadBookFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_BOOK_ID, bookId)
            }
        }
    }

    override val featureName: String = "read_book"

    @Inject
    internal lateinit var feature: ReadBookFeature

    @Inject
    internal lateinit var vibrationManager: VibrationManager
    private val bookTextStyle: TextStyle = TextStyle(fontSize = 18.sp)
    private val textPageSplitter = TextPageSplitter()
    private val bookId: Long by lazy { requireArguments().getLong(EXTRA_BOOK_ID) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setContent {
            ReadBookScreen(feature.state)
        }
    }

    @Composable
    private fun ReadBookScreen(state: ReadBookFeature.State) {
        FoboReaderTheme {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(text = "Read book")
                    }, elevation = 12.dp
                )
            }, content = { contentPadding ->
                when (state) {
                    is ReadBookFeature.State.Loading -> LoadingComposable()
                    is ReadBookFeature.State.Content -> ContentComposable(state, contentPadding)
                }
            })
        }
    }

    @Composable
    private fun LoadingComposable() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize(),
        ) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        }
    }

    @Composable
    private fun ContentComposable(state: ReadBookFeature.State.Content, contentPadding: PaddingValues) {
        if (state.paragraphTranslation != null) {
            val paragraphTranslation = state.paragraphTranslation
            val onClick: () -> Unit = {
                feature.sendIntent(ReadBookFeature.Intent.HideParagraphTranslation)
            }
            Column {
                Text(
                    modifier = Modifier.pointerInput(onClick) {
                        detectTapGestures(
                            onTap = {
                                onClick()
                            }
                        )
                    },
                    style = TextStyle(color = Color.White, fontSize = bookTextStyle.fontSize),
                    text = AnnotatedString.Builder().apply {
                        append(paragraphTranslation.sourceText)
                        append(
                            AnnotatedString(
                                text = paragraphTranslation.getMostPreciseTranslation().orEmpty(),
                                SpanStyle(color = Color.Red)
                            )
                        )
                    }.toAnnotatedString(),
                )
            }
        } else {
            PaginatedTextScreen(longText = state.text, contentPadding = contentPadding)
            if (state.wordTranslation != null) {
                TranslationPopupBox(state.wordTranslation) {
                    feature.sendIntent(ReadBookFeature.Intent.HideWordTranslation)
                }
            }
        }
    }

    @Px
    private fun getHorizontalPaddingInPx(contentPadding: PaddingValues, screenDensity: Density): Int {
        val startPadding = with(screenDensity) { contentPadding.calculateStartPadding(LayoutDirection.Ltr).toPx() }
        val endPadding = with(screenDensity) { contentPadding.calculateEndPadding(LayoutDirection.Ltr).toPx() }
        return (startPadding + endPadding).toInt()
    }

    @Px
    private fun getVerticalPaddingInPx(contentPadding: PaddingValues, screenDensity: Density): Int {
        val topPadding = with(screenDensity) { contentPadding.calculateTopPadding().toPx() }
        val bottomPadding = with(screenDensity) { contentPadding.calculateBottomPadding().toPx() }
        return (topPadding + bottomPadding).toInt()
    }

    @Px
    private fun getAvailableScreenSize(screenDensity: Density, contentPadding: PaddingValues): Pair<Int, Int> {
        val toolbarHeight = with(screenDensity) { 64.dp.toPx() }
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight: Int = displayMetrics.heightPixels
        val contentHorizontalPadding = getHorizontalPaddingInPx(contentPadding, screenDensity)
        val availableScreenHeight = screenHeight - toolbarHeight - contentHorizontalPadding
        val screenWidth: Int = displayMetrics.widthPixels
        val availableScreenWidth = screenWidth - getVerticalPaddingInPx(contentPadding, screenDensity)
        return availableScreenHeight.toInt() to availableScreenWidth
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PaginatedTextScreen(longText: String, contentPadding: PaddingValues) {
        val textMeasurer = rememberTextMeasurer()
        val (availableScreenHeight, availableScreenWidth) = getAvailableScreenSize(
            LocalDensity.current,
            contentPadding
        )
        val textPages = textPageSplitter.splitTextToPages(
            text = longText,
            measurer = textMeasurer,
            availableHeight = availableScreenHeight,
            availableWidth = availableScreenWidth,
            bookTextStyle = bookTextStyle,
        )
        val pageCount = remember { textPages.count() }
        val pagerState = rememberPagerState(pageCount = { pageCount })
        HorizontalPager(state = pagerState) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 0.dp)
            ) {
                val currentPageIndex = it
                val pageContent = textPages[currentPageIndex]
                CombinedClickableText(fullText = pageContent)
            }
        }
    }

    @Composable
    fun TranslationPopupBox(
        wordTranslation: TextTranslation,
        onClickOutside: () -> Unit,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Popup(
                alignment = Alignment.Center,
                properties = PopupProperties(
                    excludeFromSystemGesture = true,
                ),
                onDismissRequest = { onClickOutside() }
            ) {
                Box(
                    Modifier
                        .background(Color.White)
                        .padding(20.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column {
                        PopupTitledText(title = "Original: ", text = wordTranslation.sourceText)
                        PopupTitledText(
                            title = "Translation: ",
                            text = wordTranslation.getMostPreciseTranslation().orEmpty()
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun PopupTitledText(title: String, text: String) {
        Text(
            text = AnnotatedString.Builder().apply {
                append(
                    AnnotatedString(
                        title,
                        SpanStyle(color = Color.DarkGray, fontWeight = FontWeight.Medium)
                    )
                )
                append(
                    AnnotatedString(
                        text,
                        SpanStyle(color = Color.Black)
                    )
                )
            }.toAnnotatedString()
        )
    }

    private fun getSelectedParagraph(text: String, selectionStart: Int, selectionEnd: Int): String {
        val paragraphStart = findLeftLineBreak(text, selectionStart)
        val paragraphEnd = findRightLineBreak(text, selectionEnd)
        return text.substring(paragraphStart, paragraphEnd)
    }

    private fun findLeftLineBreak(text: CharSequence, selStart: Int): Int {
        for (i in selStart downTo 0) {
            if (text[i] == '\n') return i + 1
        }
        return 0
    }

    private fun findRightLineBreak(text: CharSequence, selEnd: Int): Int {
        for (i in selEnd until text.length) {
            if (text[i] == '\n') return i + 1
        }
        return text.length - 1
    }

    @Composable
    private fun CombinedClickableText(fullText: String, modifier: Modifier = Modifier) {
        val wordsPositions = getWordsPositions(fullText)
        val annotatedString = buildAnnotatedString {
            append(fullText)
            wordsPositions.forEach { (wordStart, wordEnd) ->
                addStringAnnotation(
                    tag = "word",
                    annotation = fullText.substring(wordStart, wordEnd),
                    start = wordStart,
                    end = wordEnd
                )
            }
        }
        val onLongClick = { pos: Int ->
            wordsPositions.find { pos in it.first..it.second }?.let { (start, end) ->
                val word = fullText.substring(start, end)
                feature.sendIntent(ReadBookFeature.Intent.TranslateWord(word))
            }
        }
        val onClick = { pos: Int ->
            wordsPositions.find { pos in it.first..it.second }?.let { (start, end) ->
                val paragraph = getSelectedParagraph(fullText, start, end)
                feature.sendIntent(ReadBookFeature.Intent.TranslateParagraph(paragraph))
            }
        }
        val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
        val gesture = Modifier.pointerInput(onClick, onLongClick) {
            detectTapGestures(
                onTap = { pos ->
                    layoutResult.value?.let { layout ->
                        onClick(layout.getOffsetForPosition(pos))
                    }
                },
                onLongPress = { pos ->
                    layoutResult.value?.let { layout ->
                        onLongClick(layout.getOffsetForPosition(pos))
                    }
                }
            )
        }
        Text(
            modifier = modifier.then(gesture),
            style = TextStyle(color = Color.White, fontSize = bookTextStyle.fontSize),
            text = annotatedString,
            onTextLayout = {
                layoutResult.value = it
            }
        )
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerReadBookComponent.factory().create(
                bookId,
                apiHolder().networkApi(),
                apiHolder().navigationApi(),
                commonApi(),
                apiHolder().settingsApi()
            )
        }.inject(this)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
    }

    private fun showState(state: ReadBookFeature.State) {
        // updateBookTextSize(state.textSize)
        // tvName.text = state.title
        // updatePagesRead(state.currentPage, state.totalPages)
        // tvReadPercent.text = state.readPercent.toString()
    }

    // private fun updateBookTextSize(textSize: Float?) = with(binding) {
    //     textSize?.let {
    //         tvText.textSize = it
    //     }
    // }

    // private fun updatePagesRead(currentPage: Int, totalPages: Int) = with(binding) {
    //     tvRead.text = getString(
    //         R.string.read_book_user_read_pages,
    //         currentPage,
    //         totalPages
    //     )
    // }

    // private fun showBookText(text: String) {
    //     val textHashCode = text.hashCode()
    //     if (textHashCode != lastTextHashCode) {
    //         binding.tvText.setup(text.toHtml())
    //         lastTextHashCode = textHashCode
    //         binding.tvText.setOnClickListener(null)
    //     }
    // }

    private fun takeEffect(effect: ReadBookFeature.Effect) {
        when (effect) {
            is ReadBookFeature.Effect.ShowSnackbar -> showSnackbar(effect.messageId)
            is ReadBookFeature.Effect.Vibrate -> vibrationManager.vibrate(requireView())
        }
    }
}