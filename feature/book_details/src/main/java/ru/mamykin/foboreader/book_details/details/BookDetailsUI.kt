package ru.mamykin.foboreader.book_details.details

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.book_details.rate.RateBookBottomSheetUI
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable
import ru.mamykin.foboreader.uikit.compose.TextStyles
import java.util.Locale

@Composable
fun BookDetailsUI(appNavController: NavHostController) {
    val viewModel: BookDetailsViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(BookDetailsViewModel.Intent.LoadBookInfo)
    }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, appNavController)
        }
    }
    BookDetailsScreenComposable(
        state = viewModel.state,
        onIntent = viewModel::sendIntent,
        appNavController = appNavController,
    )
}

private fun takeEffect(effect: BookDetailsViewModel.Effect, appNavController: NavHostController) = when (effect) {
    is BookDetailsViewModel.Effect.NavigateToReadBook -> {
        appNavController.navigate(AppScreen.ReadBook.createRoute(effect.bookId))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookDetailsScreenComposable(
    state: BookDetailsViewModel.State,
    onIntent: (BookDetailsViewModel.Intent) -> Unit,
    appNavController: NavHostController,
) {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = (state as? BookDetailsViewModel.State.Content)?.bookDetails?.title.orEmpty())
        }, navigationIcon = {
            IconButton(onClick = {
                appNavController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    modifier = Modifier,
                    contentDescription = null,
                )
            }
        })
    }, content = { innerPadding ->
        if (state is BookDetailsViewModel.State.Content && state.isRateDialogShown) {
            RateBookBottomSheetComposable(state, onIntent)
        }
        Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
            when (state) {
                is BookDetailsViewModel.State.Loading -> GenericLoadingIndicatorComposable()
                is BookDetailsViewModel.State.Content -> LoadedComposable(state, onIntent)
            }
        }
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RateBookBottomSheetComposable(
    state: BookDetailsViewModel.State.Content,
    onIntent: (BookDetailsViewModel.Intent) -> Unit
) {
    ModalBottomSheet(onDismissRequest = {
        onIntent(BookDetailsViewModel.Intent.SaveBookRating(null))
    }) {
        RateBookBottomSheetUI(
            bookId = state.bookDetails.id,
            onRated = {
                onIntent(BookDetailsViewModel.Intent.SaveBookRating(it))
            },
        )
    }
}

@Composable
private fun LoadedComposable(
    state: BookDetailsViewModel.State.Content,
    onIntent: (BookDetailsViewModel.Intent) -> Unit
) {
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
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
            if (state.isReadButtonEnabled) {
                FloatingActionButton(
                    shape = CircleShape,
                    onClick = {
                        onIntent(BookDetailsViewModel.Intent.OpenBook)
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_book_read),
                        contentDescription = null,
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
                .let {
                    if (!state.bookDetails.isRatedByUser) {
                        it.clickable(remember { MutableInteractionSource() }, indication = rememberRipple()) {
                            onIntent(BookDetailsViewModel.Intent.RateBook)
                        }
                    } else it
                }
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                tint = if (state.bookDetails.isRatedByUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.size(16.dp),
                contentDescription = null,
            )
            Text(
                text = String.format(Locale.UK, "%.1f", state.bookDetails.rating),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 4.dp),
            )
        }

        Text(
            text = stringResource(R.string.bd_author_title),
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp),
        )
        Text(
            text = state.bookDetails.author,
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            text = stringResource(R.string.my_books_book_genre),
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = state.bookDetails.genre,
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            text = stringResource(R.string.bd_languages_title),
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = state.bookDetails.languages.joinToString(", "),
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            text = stringResource(R.string.my_books_bookmarks),
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp),
        )
        Text(
            text = stringResource(R.string.my_books_no_bookmarks),
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            text = stringResource(R.string.bd_read_progress_title),
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp),
        )
        LinearProgressIndicator(
            progress = state.bookDetails.readPercent,
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )
    }
}

@Preview
@Composable
private fun MyBooksScreenPreview() {
    FoboReaderTheme {
        BookDetailsScreenComposable(
            state = BookDetailsViewModel.State.Content(
                bookDetails = BookInfoUIModel(
                    0L,
                    "Author",
                    "Title",
                    "https://m.media-amazon.com/images/I/41urypNXYyL.jpg",
                    "/dev/null",
                    10,
                    "Genre",
                    listOf("English, Russian, Spanish"),
                    readPercent = 0.5f,
                    rating = 4.5f,
                    isRatedByUser = false,
                ),
                isReadButtonEnabled = true,
                isRateDialogShown = true,
            ),
            onIntent = {},
            appNavController = rememberNavController(),
        )
    }
}