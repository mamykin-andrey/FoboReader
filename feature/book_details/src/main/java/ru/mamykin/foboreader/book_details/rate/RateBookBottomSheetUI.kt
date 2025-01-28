package ru.mamykin.foboreader.book_details.rate

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.book_details.details.BookInfoUIModel
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericErrorStubComposable
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun RateBookBottomSheetUI(bookId: Long, onRated: (rating: Int) -> Unit) {
    val viewModel: RateBookViewModel = hiltViewModel()
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, onRated)
        }
    }
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(RateBookViewModel.Intent.LoadBookInfo(bookId))
    }
    RateBookBottomSheetScreen(viewModel.state, viewModel::sendIntent)
}

private fun takeEffect(effect: RateBookViewModel.Effect, onRated: (rating: Int) -> Unit) = when (effect) {
    is RateBookViewModel.Effect.CloseScreen -> {
        onRated(effect.rating)
    }
}

@Composable
private fun RateBookBottomSheetScreen(
    state: RateBookViewModel.State,
    onIntent: (RateBookViewModel.Intent) -> Unit,
) {
    when (state) {
        is RateBookViewModel.State.Loading -> {
            GenericLoadingIndicatorComposable()
        }

        is RateBookViewModel.State.Content -> {
            RateBookContentBottomSheetScreen(state, onIntent)
        }

        is RateBookViewModel.State.Failed -> {
            GenericErrorStubComposable {
                onIntent(RateBookViewModel.Intent.ReloadBookInfo)
            }
        }
    }
}

@Composable
private fun RateBookContentBottomSheetScreen(
    state: RateBookViewModel.State.Content,
    onIntent: (RateBookViewModel.Intent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        AsyncImage(
            model = state.bookDetails.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .height(160.dp)
                .width(160.dp)
                .padding(12.dp),
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.img_no_image),
        )
        Text(
            text = state.bookDetails.title,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = state.bookDetails.author,
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            stringResource(id = R.string.rb_rate_nudge_title),
            modifier = Modifier.padding(top = 24.dp)
        )
        StarsRowComposable(state, onIntent)
        OutlinedButton(
            onClick = { onIntent(RateBookViewModel.Intent.SubmitRating) },
            modifier = Modifier.padding(top = 8.dp),
        ) {
            if (state.isSubmitInProgress) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = stringResource(id = R.string.rb_submit_button_title))
            }
        }
    }
}

@Composable
private fun StarsRowComposable(
    state: RateBookViewModel.State.Content,
    onIntent: (RateBookViewModel.Intent) -> Unit,
) {
    Row {
        for (index in 1..5) {
            AnimatedStarIcon(
                index = index,
                selectedRating = state.selectedRating ?: 0,
                onClick = {
                    onIntent(RateBookViewModel.Intent.SelectRating(index))
                }
            )
        }
    }
}

@Composable
private fun AnimatedStarIcon(
    index: Int,
    selectedRating: Int,
    onClick: () -> Unit
) {
    val duration = remember { 200 }
    val isSelected = index <= selectedRating
    var isAnimating by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isAnimating, label = "starTransition")
    val scale by transition.animateFloat(
        transitionSpec = {
            when {
                targetState ->
                    keyframes {
                        durationMillis = duration
                        1.0f at 0
                        2.0f at (duration / 2)
                        1.0f at duration
                    }

                else -> spring(stiffness = Spring.StiffnessLow)
            }
        }, label = ""
    ) { if (it) 2.0f else 1f }

    LaunchedEffect(isSelected) {
        if (isSelected) {
            delay(index * 50L)
            isAnimating = true
            delay(duration.toLong())
            isAnimating = false
        }
    }
    Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "Star $index",
        tint = if (isSelected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(12.dp)
            .size(28.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable { onClick() }
    )
}

@Composable
@Preview
fun RateBookBottomSheetUIPreview() {
    FoboReaderTheme {
        RateBookBottomSheetScreen(
            state = RateBookViewModel.State.Content(
                bookDetails = BookInfoUIModel(
                    id = 0,
                    author = "",
                    title = "",
                    coverUrl = "",
                    filePath = "",
                    currentPage = 0,
                    genre = "",
                    languages = listOf("Language1", "Language2", "Language3", "Language4"),
                    readPercent = 0.5f,
                    rating = 4.5f,
                    isRatedByUser = false,
                ),
                selectedRating = null,
                isSubmitInProgress = true,
            ),
            onIntent = {},
        )
    }
}