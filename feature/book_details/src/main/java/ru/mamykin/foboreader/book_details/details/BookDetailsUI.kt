package ru.mamykin.foboreader.book_details.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles

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
            Text(text = stringResource(id = R.string.my_books_book_info_title))
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
        Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
            when (state) {
                is BookDetailsViewModel.State.Loading -> LoadingComposable()
                is BookDetailsViewModel.State.Loaded -> LoadedComposable(state, onIntent)
            }
        }
    })
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
private fun LoadedComposable(
    state: BookDetailsViewModel.State.Loaded,
    onIntent: (BookDetailsViewModel.Intent) -> Unit
) {
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                AsyncImage(
                    model = state.bookDetails.coverUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .height(120.dp)
                        .width(100.dp),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.img_no_image),
                )
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = state.bookDetails.title,
                        style = TextStyles.Subtitle1,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(text = state.bookDetails.author)
                }
            }
            FloatingActionButton(
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
        Text(
            text = stringResource(R.string.my_books_bookmarks),
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = stringResource(R.string.my_books_no_bookmarks),
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            text = stringResource(R.string.my_books_book_path),
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = state.bookDetails.filePath,
            style = TextStyles.Body2,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 8.dp, start = 16.dp, end = 16.dp)
        )

        Text(
            text = stringResource(R.string.my_books_current_page),
            style = TextStyles.Subtitle1,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .padding(top = 12.dp, start = 16.dp, end = 16.dp)
        )
        Text(
            text = state.bookDetails.currentPage.toString(),
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
    }
}

@Preview
@Composable
private fun MyBooksScreenPreview() {
    FoboReaderTheme {
        BookDetailsScreenComposable(
            state = BookDetailsViewModel.State.Loaded(
                BookDetails(
                    "Author",
                    "Title",
                    "https://m.media-amazon.com/images/I/41urypNXYyL.jpg",
                    "/dev/null",
                    10,
                    "Genre"
                )
            ),
            onIntent = {},
            appNavController = rememberNavController(),
        )
    }
}