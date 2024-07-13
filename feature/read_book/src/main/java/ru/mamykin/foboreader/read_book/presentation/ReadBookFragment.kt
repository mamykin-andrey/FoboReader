package ru.mamykin.foboreader.read_book.presentation

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Px
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.di.DaggerReadBookComponent
import ru.mamykin.foboreader.read_book.platform.VibrationManager
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import javax.inject.Inject

// TODO: Optimize the Composable functions
class ReadBookFragment : BaseFragment(R.layout.fragment_read_book) {

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

    // private val wordTranslationPopup by lazy { WordTranslationPopup(requireContext()) }
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

    // TODO: keep the line break
    @Composable
    private fun ContentComposable(state: ReadBookFeature.State.Content, contentPadding: PaddingValues) {
        PaginatedTextScreen(longText = state.text, contentPadding = contentPadding)
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
                Toast.makeText(requireContext(), "Clicked word: ${fullText.substring(start, end)}", Toast.LENGTH_LONG)
                    .show()
            }
        }
        val onClick = { pos: Int ->
            wordsPositions.find { pos in it.first..it.second }?.let { (start, end) ->
                Toast.makeText(
                    requireContext(),
                    "Clicked paragraph: ${fullText.substring(start, end)}",
                    Toast.LENGTH_LONG
                ).show()
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
        initBookTextViews()
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
    }

    private fun initBookTextViews() {
        // binding.tvText.setOnParagraphClickListener {
        //     feature.sendIntent(ReadBookFeature.Intent.TranslateParagraph(it))
        // }
        // binding.tvText.setOnWordLongClickListener {
        //     feature.sendIntent(ReadBookFeature.Intent.TranslateWord(it))
        // }
    }

    private fun showState(state: ReadBookFeature.State) {
        // pbLoadingBook.isVisible = state.isTranslationLoading
        // updateBookTextSize(state.textSize)
        // showBookText(state.text)
        // updateWordTranslation(state.wordTranslation)
        // state.paragraphTranslation?.let(::showParagraphTranslation)
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

    // private fun showParagraphTranslation(translation: CharSequence) {
    //     binding.tvText.text = translation
    //     binding.tvText.setOnClickListener {
    //         feature.sendIntent(ReadBookFeature.Intent.HideParagraphTranslation)
    //     }
    //     lastTextHashCode = 0
    // }

    // private fun updateWordTranslation(translation: TextTranslation?) {
    //     if (translation != null) {
    //         wordTranslationPopup.show(requireView(), translation) {
    //             feature.sendIntent(ReadBookFeature.Intent.HideWordTranslation)
    //         }
    //     } else {
    //         wordTranslationPopup.dismiss()
    //     }
    // }

    private fun takeEffect(effect: ReadBookFeature.Effect) {
        when (effect) {
            is ReadBookFeature.Effect.ShowSnackbar -> showSnackbar(effect.messageId)
            is ReadBookFeature.Effect.Vibrate -> vibrationManager.vibrate(requireView())
        }
    }
}