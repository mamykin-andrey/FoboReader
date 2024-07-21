package ru.mamykin.foboreader.read_book.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.read_book.di.DaggerReadBookComponent
import ru.mamykin.foboreader.read_book.domain.model.TextTranslation
import ru.mamykin.foboreader.read_book.platform.VibrationManager
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

// TODO: P0 - Fix the loading on the main thread issue
// TODO: P0 - Implement the rest of the features
// TODO: P1 - Fix handing clicks by the text view when the popup is shown
// TODO: P2 - Optimize the Composable functions
// TODO: P2 - Add support of paragraph translation pagination
// TODO: P3 - Remove the split/find words/paragraphs logic from UI
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
    private val bookTextPadding = 24.dp
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
            }, content = { _ ->
                when (state) {
                    is ReadBookFeature.State.Loading -> LoadingComposable()
                    is ReadBookFeature.State.Content -> ContentComposable(state)
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
    private fun ContentComposable(state: ReadBookFeature.State.Content) {
        state.paragraphTranslation?.let {
            ParagraphTranslationComposable(it)
        } ?: BookTextComposable(state)
    }

    @Composable
    private fun BookTextComposable(
        state: ReadBookFeature.State.Content
    ) {
        PaginatedTextComposable(longText = state.text)
        if (state.wordTranslation != null) {
            TranslationPopupBox(state.wordTranslation) {
                feature.sendIntent(ReadBookFeature.Intent.HideWordTranslation)
            }
        }
    }

    @Composable
    private fun ParagraphTranslationComposable(paragraphTranslation: TextTranslation) {
        val onClick: () -> Unit = {
            feature.sendIntent(ReadBookFeature.Intent.HideParagraphTranslation)
        }
        Text(
            modifier = Modifier
                .padding(bookTextPadding)
                .pointerInput(onClick) {
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun PaginatedTextComposable(longText: String) {
        var isViewMeasured by remember { mutableStateOf(false) }
        var viewHeight by remember { mutableIntStateOf(0) }
        var viewWidth by remember { mutableIntStateOf(0) }
        if (isViewMeasured) {
            val textMeasurer = rememberTextMeasurer()
            val (availableScreenHeight, availableScreenWidth) = viewHeight to viewWidth
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
                val currentPageIndex = it
                val pageContent = textPages[currentPageIndex]
                CombinedClickableText(
                    fullText = pageContent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bookTextPadding)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bookTextPadding)
                    .onGloballyPositioned {
                        isViewMeasured = true
                        viewHeight = it.size.height
                        viewWidth = it.size.width
                    }
            )
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

    @Composable
    private fun CombinedClickableText(fullText: String, modifier: Modifier) {
        val wordsPositions: List<Pair<Int, Int>> = TextUtils.getWordsPositions(fullText)
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
            feature.sendIntent(ReadBookFeature.Intent.TranslateWord(TextUtils.getWord(wordsPositions, pos, fullText)))
        }
        val onClick = { pos: Int ->
            val paragraph = TextUtils.getParagraph(wordsPositions, pos, fullText)
            feature.sendIntent(ReadBookFeature.Intent.TranslateParagraph(paragraph))
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