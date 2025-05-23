package ru.mamykin.foboreader.my_books.list

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import ru.mamykin.foboreader.core.extension.showSnackbarWithData
import ru.mamykin.foboreader.core.navigation.AppScreen
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.sort.SortOrder
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.GenericLoadingIndicatorComposable
import ru.mamykin.foboreader.uikit.compose.TextStyles

@Composable
fun MyBooksScreen(appNavController: NavHostController) {
    val viewModel: MyBooksViewModel = hiltViewModel()
    LaunchedEffect(viewModel) {
        viewModel.sendIntent(MyBooksViewModel.Intent.LoadBooks)
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(
                effect = it,
                snackbarHostState = snackbarHostState,
                context = context,
                appNavController = appNavController,
            )
        }
    }
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    MyBooksScreenUI(
        state = state,
        onIntent = viewModel::sendIntent,
        snackbarHostState = snackbarHostState,
    )
}

private suspend fun takeEffect(
    effect: MyBooksViewModel.Effect,
    snackbarHostState: SnackbarHostState,
    context: Context,
    appNavController: NavHostController,
) {
    when (effect) {
        is MyBooksViewModel.Effect.ShowSnackbar -> {
            snackbarHostState.showSnackbarWithData(effect.data, context)
        }

        is MyBooksViewModel.Effect.NavigateToBookDetails -> {
            appNavController.navigate(AppScreen.BookDetails.createRoute(effect.bookId, true))
        }

        is MyBooksViewModel.Effect.NavigateToReadBook -> {
            appNavController.navigate(AppScreen.ReadBook.createRoute(effect.bookId))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyBooksScreenUI(
    state: MyBooksViewModel.State,
    onIntent: (MyBooksViewModel.Intent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            val searchQuery = (state as? MyBooksViewModel.State.Content)?.searchQuery
            TopAppBar(
                windowInsets = WindowInsets(top = 0.dp),
                title = {
                    if (searchQuery == null) {
                        Text(text = stringResource(id = R.string.my_books_screen_title))
                    }
                },
                actions = {
                    if (searchQuery != null) {
                        SearchFieldComposable(searchQuery, onIntent)
                    } else {
                        Row {
                            SearchButtonComposable {
                                onIntent(MyBooksViewModel.Intent.ShowSearch)
                            }
                            SortBooksComposable(onIntent)
                        }
                    }
                }
            )
        }, content = { innerPadding ->
            Box(modifier = Modifier.padding(top = innerPadding.calculateTopPadding())) {
                when (state) {
                    is MyBooksViewModel.State.Loading -> GenericLoadingIndicatorComposable()
                    is MyBooksViewModel.State.Content -> ContentComposable(state, onIntent)
                }
            }
        })
}

@Composable
private fun SearchFieldComposable(searchQuery: String, onIntent: (MyBooksViewModel.Intent) -> Unit) {
    val closeSearch = { onIntent(MyBooksViewModel.Intent.CloseSearch) }
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { newQuery ->
            onIntent(MyBooksViewModel.Intent.FilterBooks(newQuery))
        },
        placeholder = { Text(text = stringResource(R.string.my_books_menu_search)) },
        singleLine = true,
        trailingIcon = {
            IconButton(onClick = closeSearch) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
            .focusRequester(focusRequester)
    )
    BackHandler(onBack = closeSearch)
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun SearchButtonComposable(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
        )
    }
}

@Composable
private fun SortBooksComposable(onIntent: (MyBooksViewModel.Intent) -> Unit) {
    val isPopupExpanded = remember { mutableStateOf(false) }
    IconButton(onClick = {
        isPopupExpanded.value = true
    }) {
        Icon(
            imageVector = Icons.Default.Sort,
            contentDescription = null,
        )
    }
    Box {
        DropdownMenu(
            expanded = isPopupExpanded.value,
            onDismissRequest = { isPopupExpanded.value = false },
        ) {
            DropdownMenuItem(
                text = {
                    Text(stringResource(R.string.my_books_sort_by_name))
                },
                onClick = {
                    isPopupExpanded.value = false
                    onIntent(MyBooksViewModel.Intent.SortBooks(SortOrder.ByName))
                }
            )
            DropdownMenuItem(
                onClick = {
                    isPopupExpanded.value = false
                    onIntent(MyBooksViewModel.Intent.SortBooks(SortOrder.ByReadPages))
                },
                text = {
                    Text(stringResource(R.string.my_books_sort_by_readed))
                }
            )
            DropdownMenuItem(
                onClick = {
                    isPopupExpanded.value = false
                    onIntent(MyBooksViewModel.Intent.SortBooks(SortOrder.ByDate))
                }, text = {
                    Text(stringResource(R.string.my_books_sort_by_date))
                }
            )
        }
    }
}

@Composable
private fun ContentComposable(state: MyBooksViewModel.State.Content, onIntent: (MyBooksViewModel.Intent) -> Unit) {
    val books = state.books
    if (books.isEmpty()) {
        NoBooksComposable()
    } else {
        Column {
            state.books.forEach { BookRowComposable(it, onIntent) }
        }
    }
}

@Composable
private fun BookRowComposable(book: BookInfoUIModel, onIntent: (MyBooksViewModel.Intent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        onClick = {
            onIntent(MyBooksViewModel.Intent.OpenBook(book.id))
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            AsyncImage(
                model = book.coverUrl,
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
                    .align(Alignment.Top)
                    .padding(0.dp)
            ) {
                BookContextActionsComposable(book, onIntent)
                BookAuthorComposable(book)
                BookGenreComposable(book)
                BookLanguagesComposable(book)
                LinearProgressIndicator(
                    progress = book.readPercent,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(horizontal = 12.dp),
                )
                BookReadStatusComposable(book)
            }
        }
    }
}

@Composable
private fun BookContextActionsComposable(
    book: BookInfoUIModel,
    onIntent: (MyBooksViewModel.Intent) -> Unit
) {
    val isBookPopupExpanded = remember { mutableStateOf(false) }
    Row {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            Text(
                text = book.title,
                style = TextStyles.Body2,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    isBookPopupExpanded.value = true
                }, modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                )
            }
        }
        Box {
            DropdownMenu(
                expanded = isBookPopupExpanded.value,
                onDismissRequest = { isBookPopupExpanded.value = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        isBookPopupExpanded.value = false
                        onIntent(MyBooksViewModel.Intent.OpenBookDetails(book.id))
                    },
                    text = {
                        Text("About")
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        isBookPopupExpanded.value = false
                        onIntent(MyBooksViewModel.Intent.RemoveBook(book.id))
                    }, text = {
                        Text("Remove")
                    }
                )
            }
        }
    }
}

@Composable
private fun BookAuthorComposable(book: BookInfoUIModel) {
    Text(
        text = book.author,
        style = TextStyles.Body2,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 12.dp),
    )
}

@Composable
private fun BookGenreComposable(book: BookInfoUIModel) {
    Text(
        text = book.genre,
        style = TextStyles.Body2,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 12.dp),
    )
}

@Composable
private fun BookLanguagesComposable(book: BookInfoUIModel) {
    Text(
        text = book.languages.joinToString(", "),
        style = TextStyles.Body2,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 12.dp),
    )
}

@Composable
private fun BookReadStatusComposable(book: BookInfoUIModel) {
    Text(
        text = book.readStatus.toString(LocalContext.current),
        style = TextStyles.Body2,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 12.dp),
    )
}

@Composable
private fun NoBooksComposable() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.my_books_no_books),
            style = TextStyles.Subtitle1,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun MyBooksScreenPreview() {
    FoboReaderTheme(darkTheme = false) {
        MyBooksScreenUI(
            state = MyBooksViewModel.State.Content(
                books = listOf(
                    BookInfoUIModel(
                        id = 0,
                        genre = "classic",
                        coverUrl = "https://m.media-amazon.com/images/I/71O2XIytdqL._AC_UF894,1000_QL80_.jpg",
                        author = "Fyodor Dostoyevsky",
                        title = "Crime & Punishment",
                        languages = listOf("English, Russian, Spanish"),
                        readStatus = StringOrResource.String("1 from 10"),
                        readPercent = 0.5f,
                    )
                )
            ),
            onIntent = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}