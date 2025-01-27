package ru.mamykin.foboreader.book_details.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun RateBookBottomSheetUI(bookId: Long, onRated: (rating: Int) -> Unit) {
    val viewModel: RateBookViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(RateBookViewModel.Intent.LoadBookInfo(bookId))
    }
    RateBookBottomSheetScreen(viewModel.state, onRated)
}

@Composable
private fun RateBookBottomSheetScreen(
    state: RateBookViewModel.State,
    onRated: (rating: Int) -> Unit,
) {
    when (state) {
        is RateBookViewModel.State.Loading -> {
            GenericLoadingIndicatorComposable()
        }

        is RateBookViewModel.State.Content -> {
            RateBookContentBottomSheetScreen(state, onRated)
        }

        is RateBookViewModel.State.Failed -> {
        }
    }
}

@Composable
private fun RateBookContentBottomSheetScreen(
    state: RateBookViewModel.State.Content,
    onRated: (rating: Int) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
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
            "How would you rate it?",
            modifier = Modifier.padding(top = 24.dp)
        )
        Row {
            IconButton(onClick = {
                onRated(1)
            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    modifier = Modifier,
                    contentDescription = null,
                )
            }
            IconButton(onClick = {
                onRated(2)
            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    modifier = Modifier,
                    contentDescription = null,
                )
            }
            IconButton(onClick = {
                onRated(3)
            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    modifier = Modifier,
                    contentDescription = null,
                )
            }
            IconButton(onClick = {
                onRated(4)
            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    modifier = Modifier,
                    contentDescription = null,
                )
            }
            IconButton(onClick = {
                onRated(5)
            }) {
                Icon(
                    imageVector = Icons.Default.Star,
                    modifier = Modifier,
                    contentDescription = null,
                )
            }
        }
        Button(
            onClick = { },
            modifier = Modifier.padding(top = 8.dp),
        ) {
            Text(text = "Submit")
        }
    }
}

@Composable
@Preview
fun RateBookBottomSheetUIPreview() {
    FoboReaderTheme {
        Scaffold {
            RateBookBottomSheetScreen(
                state = RateBookViewModel.State.Loading,
                onRated = {},
            )
        }
    }
}