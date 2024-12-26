package ru.mamykin.foboreader.my_books.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.getActivity
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.sort.SortOrder
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles
import java.util.Date

// TODO: Use viewModelStore instead
private const val SCREEN_KEY = "my_books"

private fun createAndInitViewModel(apiHolder: ApiHolder): MyBooksViewModel {
    return ComponentHolder.getOrCreateComponent(key = SCREEN_KEY) {
        DaggerMyBooksComponent.factory().create(
            apiHolder.commonApi(),
        )
    }.myBooksViewModel().also { it.sendIntent(MyBooksViewModel.Intent.LoadBooks) }
}

@Composable
fun MyBooksScreen(onBookDetailsClick: (Long) -> Unit, onReadBookClick: (Long) -> Unit) {
    val context = LocalContext.current
    val viewModel = remember { createAndInitViewModel(context.getActivity().apiHolder()) }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        onDispose {
            if (lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                ComponentHolder.clearComponent(SCREEN_KEY)
            }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel.effectFlow) {
        viewModel.effectFlow.collect {
            takeEffect(it, snackbarHostState, onBookDetailsClick, onReadBookClick)
        }
    }
    MyBooksScreenUI(viewModel.state, viewModel::sendIntent, snackbarHostState)
}

private suspend fun takeEffect(
    effect: MyBooksViewModel.Effect,
    snackbarHostState: SnackbarHostState,
    onBookDetailsClick: (Long) -> Unit,
    onReadBookClick: (Long) -> Unit
) {
    when (effect) {
        is MyBooksViewModel.Effect.ShowSnackbar -> {
            snackbarHostState.showSnackbar(effect.message)
        }

        is MyBooksViewModel.Effect.NavigateToBookDetails -> {
            onBookDetailsClick(effect.bookId)
        }

        is MyBooksViewModel.Effect.NavigateToReadBook -> {
            onReadBookClick(effect.bookId)
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
    FoboReaderTheme {
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
                        is MyBooksViewModel.State.Loading -> LoadingComposable()
                        is MyBooksViewModel.State.Content -> ContentComposable(state, onIntent)
                    }
                }
            })
    }
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
            .padding(start = 0.dp, end = 8.dp, top = 2.dp, bottom = 2.dp)
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
                    Text("By name")
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
                    Text("By read pages")
                }
            )
            DropdownMenuItem(
                onClick = {
                    isPopupExpanded.value = false
                    onIntent(MyBooksViewModel.Intent.SortBooks(SortOrder.ByDate))
                }, text = {
                    Text("By date")
                }
            )
        }
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
private fun BookRowComposable(bookInfo: BookInfo, onIntent: (MyBooksViewModel.Intent) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp), onClick = {
        onIntent(MyBooksViewModel.Intent.OpenBook(bookInfo.id))
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            AsyncImage(
                model = bookInfo.coverUrl,
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
                BookContextActionsComposable(bookInfo, onIntent)
                BookFormatComposable(bookInfo)
                BookAuthorComposable(bookInfo)
                LinearProgressIndicator(
                    progress = bookInfo.getReadPercent().toFloat() / 100,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .padding(horizontal = 12.dp),
                )
                BookReadStatusComposable(bookInfo)
            }
        }
    }
}

@Composable
private fun BookFormatComposable(bookInfo: BookInfo) {
    Text(
        text = stringResource(
            id = R.string.my_books_book_info_description_title,
            bookInfo.getFormat(),
            bookInfo.getDisplayFileSize(),
        ),
        style = TextStyles.Body2,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 12.dp),
    )
}

@Composable
private fun BookContextActionsComposable(bookInfo: BookInfo, onIntent: (MyBooksViewModel.Intent) -> Unit) {
    val isBookPopupExpanded = remember { mutableStateOf(false) }
    Row {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp)
        ) {
            Text(
                text = bookInfo.title,
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
                        onIntent(MyBooksViewModel.Intent.OpenBookDetails(bookInfo.id))
                    },
                    text = {
                        Text("About")
                    }
                )
                DropdownMenuItem(
                    onClick = {
                        isBookPopupExpanded.value = false
                        onIntent(MyBooksViewModel.Intent.RemoveBook(bookInfo.id))
                    }, text = {
                        Text("Remove")
                    }
                )
            }
        }
    }
}

@Composable
private fun BookAuthorComposable(bookInfo: BookInfo) {
    Text(
        text = bookInfo.author,
        style = TextStyles.Body2,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier
            .padding(top = 4.dp)
            .padding(horizontal = 12.dp),
    )
}

@Composable
private fun BookReadStatusComposable(bookInfo: BookInfo) {
    Text(
        text = stringResource(
            id = R.string.book_pages_info, bookInfo.currentPage, bookInfo.totalPages ?: 0
        ),
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
    MyBooksScreenUI(
        state = MyBooksViewModel.State.Content(
            allBooks = emptyList(),
            books = listOf(
                BookInfo(
                    id = 0,
                    filePath = "",
                    genre = "classic",
                    coverUrl = "https://m.media-amazon.com/images/I/71O2XIytdqL._AC_UF894,1000_QL80_.jpg",
                    author = "Fyodor Dostoyevsky",
                    title = "Crime & Punishment",
                    languages = listOf("ru, en"),
                    date = Date(),
                    currentPage = 0,
                    totalPages = 10,
                    lastOpen = 1000,
                )
            )
        ),
        onIntent = {},
        snackbarHostState = remember { SnackbarHostState() },
    )
}