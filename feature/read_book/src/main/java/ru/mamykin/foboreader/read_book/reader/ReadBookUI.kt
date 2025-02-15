package ru.mamykin.foboreader.read_book.reader

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ru.mamykin.foboreader.core.extension.showSnackbarWithData
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.core.navigation.MainTabScreenRoutes
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.read_book.R
import ru.mamykin.foboreader.read_book.translation.TextTranslation
import ru.mamykin.foboreader.read_book.translation.WordTranslationUIModel
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable

@Composable
fun ReadBookUI(appNavController: NavHostController) {
    val viewModel: ReadBookViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(
                effect = it,
                snackbarHostState = snackbarHostState,
                context = context,
                haptic = haptic,
            )
        }
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ReadBookScreen(
        state = state,
        onIntent = viewModel::sendIntent,
        snackbarHostState = snackbarHostState,
        appNavController = appNavController,
    )
}

private suspend fun takeEffect(
    effect: ReadBookViewModel.Effect,
    snackbarHostState: SnackbarHostState,
    context: Context,
    haptic: HapticFeedback,
) {
    when (effect) {
        is ReadBookViewModel.Effect.ShowSnackbar -> {
            snackbarHostState.showSnackbarWithData(effect.data, context)
        }

        is ReadBookViewModel.Effect.Vibrate -> {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReadBookScreen(
    state: ReadBookViewModel.State,
    onIntent: (ReadBookViewModel.Intent) -> Unit,
    snackbarHostState: SnackbarHostState,
    appNavController: NavHostController,
) {
    val goBackFunc = remember {
        {
            appNavController.popBackStack(
                AppScreen.Main.createRoute(MainTabScreenRoutes.MY_BOOKS),
                false,
            )
        }
    }
    BackHandler(onBack = {
        goBackFunc()
    })
    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }, topBar = {
        TopAppBar(title = {
            Text(text = state.title.toString(LocalContext.current))
        }, navigationIcon = {
            IconButton(onClick = { goBackFunc() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = "Close"
                )
            }
        }, actions = {
            val bookId = (state as? ReadBookViewModel.State.Content)?.bookId ?: return@TopAppBar
            IconButton(onClick = {
                appNavController.navigate(
                    AppScreen.BookDetails.createRoute(
                        bookId = bookId,
                        readAllowed = false,
                    )
                )
            }) {
                Icon(
                    imageVector = Icons.Filled.Info, contentDescription = "Information"
                )
            }
        })
    }, content = { innerPadding ->
        Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
            when (state) {
                is ReadBookViewModel.State.Loading -> LoadingComposable(onIntent)
                is ReadBookViewModel.State.Content -> ContentComposable(state, onIntent)
                is ReadBookViewModel.State.Failed -> OpenBookErrorComposable(onIntent)
            }
        }
    })
}

@Composable
private fun LoadingComposable(onIntent: (ReadBookViewModel.Intent) -> Unit) {
    GenericLoadingIndicatorComposable()
    StubContentComposable(onIntent)
}

/**
 * Measures the text Composable size and sends intent to the feature
 * So it can correctly split the text by pages based on that
 */
@Composable
private fun StubContentComposable(onIntent: (ReadBookViewModel.Intent) -> Unit) {
    val textMeasurer = rememberTextMeasurer()
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .alpha(0f)
                .weight(1f)
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned {
                        onIntent(
                            ReadBookViewModel.Intent.LoadBook(
                                textMeasurer, it.size.height to it.size.width
                            )
                        )
                    }, style = TextStyle(color = Color.White, fontSize = 16.sp), text = AnnotatedString("")
            )
        }
        ReadStatusComposable(
            currentPageIndex = 0, totalPages = 0, readPercent = 0f, modifier = Modifier.alpha(0f)
        )
    }
}

@Composable
private fun ContentComposable(state: ReadBookViewModel.State.Content, onIntent: (ReadBookViewModel.Intent) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            state.paragraphTranslation?.let {
                ParagraphTranslationComposable(it, onIntent, state)
            } ?: BookTextComposable(state, Modifier.fillMaxSize(), onIntent)
        }
        ReadStatusComposable(
            currentPageIndex = state.currentPage,
            totalPages = state.totalPages,
            readPercent = state.readPercent,
            modifier = Modifier,
        )
    }
}

@Composable
private fun ReadStatusComposable(
    currentPageIndex: Int, totalPages: Int, readPercent: Float, modifier: Modifier
) {
    Box(
        modifier = modifier.then(
            Modifier
                .background(color = Color.Gray)
                .fillMaxWidth()
        )
    ) {
        Row(modifier = Modifier.padding(4.dp)) {
            val percentStr = "%.2f".format(readPercent)
            Text(text = "${currentPageIndex + 1} of $totalPages, $percentStr%")
        }
    }
}

@Composable
private fun BookTextComposable(
    state: ReadBookViewModel.State.Content, modifier: Modifier, onIntent: (ReadBookViewModel.Intent) -> Unit
) {
    PaginatedTextComposable(state, modifier, onIntent)
    state.wordTranslation?.let { WordTranslationPopupComposable(it, onIntent) }
}

@Composable
private fun ParagraphTranslationComposable(
    paragraphTranslation: TextTranslation,
    onIntent: (ReadBookViewModel.Intent) -> Unit,
    state: ReadBookViewModel.State.Content
) {
    val onClick: () -> Unit = {
        onIntent(ReadBookViewModel.Intent.HideParagraphTranslation)
    }
    Text(
        modifier = Modifier.pointerInput(onClick) {
            detectTapGestures(onTap = {
                onClick()
            })
        },
        style = TextStyle(fontSize = state.userSettings.fontSize.sp),
        text = AnnotatedString.Builder().apply {
            append(paragraphTranslation.sourceText)
            append("\n\n")
            append(
                AnnotatedString(
                    text = paragraphTranslation.getMostPreciseTranslation().orEmpty(),
                    SpanStyle(color = Color(android.graphics.Color.parseColor(state.userSettings.translationColorCode)))
                )
            )
        }.toAnnotatedString(),
    )
}

@Composable
private fun PaginatedTextComposable(
    state: ReadBookViewModel.State.Content, modifier: Modifier, onIntent: (ReadBookViewModel.Intent) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { state.pages.size }, initialPage = state.currentPage)
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onIntent(ReadBookViewModel.Intent.PageChanged(page))
        }
    }
    HorizontalPager(state = pagerState) {
        val currentPageIndex = it
        val pageContent = state.pages[currentPageIndex]
        CombinedClickableText(
            page = pageContent,
            modifier = modifier,
            onIntent,
            state,
        )
    }
}

@Composable
private fun WordTranslationPopupComposable(
    translation: WordTranslationUIModel,
    onIntent: (ReadBookViewModel.Intent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { onIntent(ReadBookViewModel.Intent.HideWordTranslation) }
            }.background(Color.Transparent)
    )
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Popup(alignment = Alignment.Center,
            properties = PopupProperties(excludeFromSystemGesture = true),
            onDismissRequest = { onIntent(ReadBookViewModel.Intent.HideWordTranslation) }) {
            Box(
                Modifier
                    .background(MaterialTheme.colorScheme.inverseSurface)
                    .padding(20.dp)
                    .clip(RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    TextWithTitleComposable(
                        R.string.rb_translation_original,
                        translation.word,
                    )
                    TextWithTitleComposable(
                        R.string.rb_translation_translated,
                        translation.translation,
                    )
                    DictionaryCheckboxComposable(translation, onIntent)
                }
            }
        }
    }
}

@Composable
private fun TextWithTitleComposable(@StringRes titleRes: Int, text: String) {
    Text(
        text = "${stringResource(titleRes)}: $text",
        color = MaterialTheme.colorScheme.inverseOnSurface,
        textAlign = TextAlign.Center,
    )
}

@Composable
private fun DictionaryCheckboxComposable(
    translation: WordTranslationUIModel,
    onIntent: (ReadBookViewModel.Intent) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
        Checkbox(
            checked = translation.dictionaryId != null,
            onCheckedChange = { isChecked ->
                if (isChecked) {
                    onIntent(ReadBookViewModel.Intent.SaveWordToDictionary(translation.word))
                } else {
                    onIntent(ReadBookViewModel.Intent.RemoveWordFromDictionary(translation.dictionaryId!!))
                }
            }
        )
        Text(
            text = stringResource(R.string.rb_word_translation_learn_check_title),
            color = MaterialTheme.colorScheme.inverseOnSurface,
        )
    }
}

@Composable
private fun CombinedClickableText(
    page: AnnotatedString,
    modifier: Modifier,
    onIntent: (ReadBookViewModel.Intent) -> Unit,
    state: ReadBookViewModel.State.Content
) {
    val onLongClick = { pos: Int ->
        val word = page.getStringAnnotations(TextAnnotations.WORD, pos, pos).firstOrNull()?.item
        word?.let { onIntent(ReadBookViewModel.Intent.TranslateWord(it)) }
    }
    val onClick = { pos: Int ->
        val sentenceNum =
            page.getStringAnnotations(TextAnnotations.SENTENCE_NUMBER, pos, pos).firstOrNull()?.item?.toInt()
        sentenceNum?.let { onIntent(ReadBookViewModel.Intent.TranslateParagraph(it)) }
    }
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val gesture = Modifier.pointerInput(onClick, onLongClick) {
        detectTapGestures(onTap = { pos ->
            layoutResult.value?.let { layout ->
                onClick(layout.getOffsetForPosition(pos))
            }
        }, onLongPress = { pos ->
            layoutResult.value?.let { layout ->
                onLongClick(layout.getOffsetForPosition(pos))
            }
        })
    }
    val (heightDp, widthDp) = with(LocalDensity.current) {
        state.textHeight.toDp() to state.textWidth.toDp()
    }
    Text(modifier = modifier
        .then(gesture)
        .height(heightDp)
        .width(widthDp),
        style = TextStyle(fontSize = state.userSettings.fontSize.sp),
        text = page,
        onTextLayout = {
            layoutResult.value = it
        })
}

@Composable
private fun OpenBookErrorComposable(
    onIntent: (ReadBookViewModel.Intent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Unable to open the book, please try again.")
        Button(modifier = Modifier.padding(top = 12.dp), onClick = {
            onIntent(ReadBookViewModel.Intent.ReloadBook)
        }) {
            Text(text = "Try again")
        }
    }
}

@Preview
@Composable
fun ReadBookScreenPreview() {
    FoboReaderTheme {
        ReadBookScreen(
            state = ReadBookViewModel.State.Content(
                StringOrResource.String("Title"),
                0,
                listOf(AnnotatedString("Page1"), AnnotatedString("Page2")),
                300,
                300,
                ReadBookViewModel.State.Content.UserSettings(18, "222222", "444444"),
                1,
                2,
                50f,
                // wordTranslation = WordTranslationUIModel("Hello", "Bonjour", 100L)
            ),
            // state = ReadBookViewModel.State.Failed(StringOrResource.String("Title")),
            onIntent = {},
            snackbarHostState = SnackbarHostState(),
            appNavController = rememberNavController(),
        )
    }
}