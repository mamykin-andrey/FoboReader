package ru.mamykin.foboreader.learn_new_words

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable
import kotlin.math.abs
import kotlin.math.sign

@Composable
fun LearnNewWordsUI(appNavController: NavController) {
    val viewModel: LearnNewWordsViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(LearnNewWordsViewModel.Intent.LoadData)
    }
    LearnNewWordsScreenComposable(
        state = viewModel.state,
        appNavController = appNavController,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LearnNewWordsScreenComposable(
    state: LearnNewWordsViewModel.State,
    appNavController: NavController,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.mw_tab_screen_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        appNavController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            modifier = Modifier,
                            contentDescription = null,
                        )
                    }
                }
            )
        }, content = { innerPadding ->
            Box(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
            ) {
                when (state) {
                    is LearnNewWordsViewModel.State.Loading -> GenericLoadingIndicatorComposable()
                    is LearnNewWordsViewModel.State.Content -> ContentComposable(state, appNavController)
                }
            }
        })
}

@Composable
private fun ContentComposable(
    state: LearnNewWordsViewModel.State.Content,
    appNavController: NavController
) {
    val words = remember {
        listOf(
            WordCard("Hello", "Bonjour"),
            WordCard("Goodbye", "Au revoir"),
            WordCard("Thank you", "Merci")
        )
    }

    SwipeableWordCard(
        cards = words,
        onRemembered = { /* Handle remembered word */ },
        onForgotten = { /* Handle forgotten word */ }
    )
}

data class WordCard(
    val word: String,
    val translation: String
)

@Composable
fun SwipeableWordCard(
    cards: List<WordCard>,
    onRemembered: (WordCard) -> Unit,
    onForgotten: (WordCard) -> Unit,
    modifier: Modifier = Modifier
) {
    if (cards.isEmpty()) return

    val scope = rememberCoroutineScope()
    var currentIndex by remember { mutableStateOf(0) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val rotation by remember { derivedStateOf { offsetX * 0.1f } }

    val swipeThreshold = 200f
    val screenWidth = 2000f  // Approximate screen width for animation

    // Animation values
    val animatedOffsetX = remember { Animatable(0f) }
    val animatedOffsetY = remember { Animatable(0f) }
    val animatedRotation = remember { Animatable(0f) }
    var isAnimating by remember { mutableStateOf(false) }

    LaunchedEffect(currentIndex) {
        // Reset values for new card
        offsetX = 0f
        offsetY = 0f
        animatedOffsetX.snapTo(0f)
        animatedOffsetY.snapTo(0f)
        animatedRotation.snapTo(0f)
        isAnimating = false
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Next card (if available)
        if (currentIndex + 1 < cards.size) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(400.dp)
                    .graphicsLayer {
                        scaleX = 0.9f
                        scaleY = 0.9f
                        alpha = 0.5f
                    }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = cards[currentIndex + 1].word)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = cards[currentIndex + 1].translation)
                    }
                }
            }
        }

        // Current card
        if (currentIndex < cards.size) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(400.dp)
                    .graphicsLayer {
                        translationX = if (isAnimating) animatedOffsetX.value else offsetX
                        translationY = if (isAnimating) animatedOffsetY.value else offsetY
                        rotationZ = if (isAnimating) animatedRotation.value else rotation
                    }
                    .pointerInput(currentIndex) {
                        detectDragGestures(
                            onDragEnd = {
                                if (abs(offsetX) > swipeThreshold && !isAnimating) {
                                    scope.launch {
                                        isAnimating = true

                                        // Calculate target position based on current direction
                                        val direction = sign(offsetX)
                                        val targetX = direction * screenWidth

                                        // Start with current position
                                        animatedOffsetX.snapTo(offsetX)
                                        animatedOffsetY.snapTo(offsetY)
                                        animatedRotation.snapTo(rotation)

                                        // Animate to final position
                                        animatedOffsetX.animateTo(
                                            targetValue = targetX,
                                            animationSpec = tween(400, easing = LinearEasing)
                                        )

                                        if (offsetX > 0) {
                                            onRemembered(cards[currentIndex])
                                        } else {
                                            onForgotten(cards[currentIndex])
                                        }
                                        currentIndex++
                                    }
                                } else {
                                    // Reset position if not passed threshold
                                    scope.launch {
                                        animatedOffsetX.animateTo(0f, spring())
                                        animatedOffsetY.animateTo(0f, spring())
                                        offsetX = 0f
                                        offsetY = 0f
                                    }
                                }
                            },
                            onDrag = { change, dragAmount ->
                                if (!isAnimating) {
                                    change.consume()
                                    offsetX += dragAmount.x
                                    offsetY += dragAmount.y
                                }
                            }
                        )
                    }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = cards[currentIndex].word)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = cards[currentIndex].translation)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun Preview() {
    FoboReaderTheme {
        LearnNewWordsScreenComposable(
            state = LearnNewWordsViewModel.State.Content,
            appNavController = rememberNavController(),
        )
    }
}