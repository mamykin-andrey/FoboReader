package ru.mamykin.foboreader.my_books.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.github.terrakok.cicerone.Router
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.di.DaggerMyBooksComponent
import ru.mamykin.foboreader.my_books.sort.SortOrder
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles
import java.util.Date
import javax.inject.Inject

class MyBooksFragment : BaseFragment() {

    companion object {

        fun newInstance(): Fragment = MyBooksFragment()
    }

    override val featureName: String = "my_books"

    @Inject
    internal lateinit var feature: MyBooksFeature

    @Inject
    internal lateinit var router: Router

    @Inject
    internal lateinit var screenProvider: ScreenProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            MyBooksScreen(feature.state)
        }
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerMyBooksComponent.factory().create(
                apiHolder().navigationApi(),
                commonApi(),
            )
        }.inject(this)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFeature()
    }

    private fun initFeature() {
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
    }

    private fun takeEffect(effect: MyBooksFeature.Effect) = when (effect) {
        is MyBooksFeature.Effect.ShowSnackbar -> showSnackbar(effect.message)
    }

    @Composable
    private fun MyBooksScreen(state: MyBooksFeature.State) {
        FoboReaderTheme {
            Scaffold(topBar = {
                TopAppBar(title = {
                    Text(text = stringResource(id = R.string.my_books_screen_title))
                }, elevation = 12.dp, actions = {
                    Row {
                        SearchBooksComposable()
                        SortBooksComposable()
                    }
                })
            }, content = {
                when (state) {
                    is MyBooksFeature.State.Loading -> LoadingComposable()
                    is MyBooksFeature.State.Content -> ContentComposable(state)
                }
            })
        }
    }

    @Composable
    private fun SearchBooksComposable() {
        IconButton(onClick = {
            TODO("Not implemented")
            //     val searchView = menu.getSearchView(R.id.action_search)
            //     searchView.queryHint = getString(R.string.my_books_menu_search)
            //     searchView.setQueryChangedListener {
            //         feature.sendEvent(MyBooksFeature.Event.FilterTextChanged(it))
            //     }
        }) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
            )
        }
    }

    @Composable
    private fun SortBooksComposable() {
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
                DropdownMenuItem(onClick = {
                    isPopupExpanded.value = false
                    feature.sendIntent(MyBooksFeature.Intent.SortBooks(SortOrder.ByName))
                }) {
                    Text("By name")
                }
                DropdownMenuItem(onClick = {
                    isPopupExpanded.value = false
                    feature.sendIntent(MyBooksFeature.Intent.SortBooks(SortOrder.ByReadPages))
                }) {
                    Text("By read pages")
                }
                DropdownMenuItem(onClick = {
                    isPopupExpanded.value = false
                    feature.sendIntent(MyBooksFeature.Intent.SortBooks(SortOrder.ByDate))
                }) {
                    Text("By date")
                }
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
    private fun ContentComposable(state: MyBooksFeature.State.Content) {
        val books = state.books
        if (books.isEmpty()) {
            NoBooksComposable()
        } else {
            Column {
                state.books.forEach { BookRowComposable(it) }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    private fun BookRowComposable(bookInfo: BookInfo) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            elevation = 16.dp,
            onClick = {
                router.navigateTo(screenProvider.readBookScreen(bookInfo.id))
            }
        ) {
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
                    BookContextActionsComposable(bookInfo)
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
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier
                .padding(top = 4.dp)
                .padding(horizontal = 12.dp),
        )
    }

    @Composable
    private fun BookContextActionsComposable(bookInfo: BookInfo) {
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
                    color = MaterialTheme.colors.onBackground,
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
                    DropdownMenuItem(onClick = {
                        isBookPopupExpanded.value = false
                        router.navigateTo(screenProvider.bookDetailsScreen(bookInfo.id))
                    }) {
                        Text("About")
                    }
                    DropdownMenuItem(onClick = {
                        isBookPopupExpanded.value = false
                        feature.sendIntent(MyBooksFeature.Intent.RemoveBook(bookInfo.id))
                    }) {
                        Text("Remove")
                    }
                }
            }
        }
    }

    @Composable
    private fun BookAuthorComposable(bookInfo: BookInfo) {
        Text(
            text = bookInfo.author,
            style = TextStyles.Body2,
            color = MaterialTheme.colors.onBackground,
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
            color = MaterialTheme.colors.onBackground,
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
                .padding(16.dp),
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
        MyBooksScreen(
            MyBooksFeature.State.Content(
                listOf(
                    BookInfo(
                        1,
                        "",
                        "Genre",
                        null,
                        "Author",
                        "Title",
                        listOf("Lang1", "Lang2"),
                        null,
                        0,
                        null,
                        Date().time,
                    ), BookInfo(
                        2,
                        "",
                        "Genre 2",
                        null,
                        "Author 2",
                        "Title 2",
                        listOf("Lang12", "Lang22"),
                        null,
                        10,
                        20,
                        Date().time,
                    )
                )
            )
        )
    }
}