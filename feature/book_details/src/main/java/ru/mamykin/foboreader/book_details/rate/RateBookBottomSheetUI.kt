package ru.mamykin.foboreader.book_details.rate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
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
        for (i in 1..5) {
            IconButton(onClick = {
                onIntent(RateBookViewModel.Intent.SelectRating(i))
            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = if (state.selectedRating != null && i <= state.selectedRating)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier,
                    contentDescription = null,
                )
            }
        }
    }
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