package ru.mamykin.foboreader.store.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showNotification
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.di.DaggerBookListComponent
import ru.mamykin.foboreader.uikit.ErrorStubWidget
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles
import javax.inject.Inject

internal class BooksListFragment : BaseFragment() {

    companion object {

        private const val EXTRA_CATEGORY_ID = "extra_category_id"

        fun newInstance(categoryId: String): Fragment = BooksListFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_CATEGORY_ID, categoryId)
            }
        }
    }

    override val featureName: String = "books_list"

    @Inject
    internal lateinit var feature: BooksListFeature

    private val categoryId by lazy { requireArguments().getString(EXTRA_CATEGORY_ID)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerBookListComponent.factory().create(
                commonApi(),
                apiHolder().networkApi(),
                apiHolder().navigationApi(),
                apiHolder().settingsApi(),
                categoryId,
            )
        }.inject(this)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            BooksListScreen(state = feature.state)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    private fun observeViewModel() {
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
    }

    private fun takeEffect(effect: BooksListFeature.Effect) {
        when (effect) {
            is BooksListFeature.Effect.ShowSnackbar -> showSnackbar(effect.message)
            is BooksListFeature.Effect.ShowNotification -> showNotification(
                notificationId = effect.notificationId,
                iconRes = effect.iconRes,
                title = effect.title,
                text = effect.text
            )
            is BooksListFeature.Effect.NavigateToMyBooks -> {
                // TODO:
            }
        }
    }

    @Composable
    private fun BooksListScreen(state: BooksListFeature.State) {
        FoboReaderTheme {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.books_store_title))
                    },
                    elevation = 12.dp,
                    navigationIcon = {
                        IconButton(onClick = {
                            requireActivity().onBackPressed()
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                modifier = Modifier,
                                contentDescription = null,
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            TODO("Not implemented")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                modifier = Modifier,
                                contentDescription = null,
                            )
                        }
                    }
                )
            }, content = {
                when (state) {
                    is BooksListFeature.State.Loading -> LoadingComposable()
                    is BooksListFeature.State.Content -> ContentComposable(state)
                    is BooksListFeature.State.Error -> ErrorComposable(state)
                }
            })
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
    private fun ContentComposable(state: BooksListFeature.State.Content) {
        Column {
            state.books.forEach { BookRowComposable(it) }
        }
    }

    @Composable
    private fun BookRowComposable(book: StoreBook) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable {
                    feature.sendIntent(BooksListFeature.Intent.DownloadBook(book))
                },
            elevation = 16.dp,
        ) {
            Row(
                modifier = Modifier.padding(16.dp)
            ) {
                AsyncImage(
                    model = book.cover,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    contentScale = ContentScale.Crop,
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(
                        text = book.title,
                        style = TextStyles.Subtitle1,
                        color = MaterialTheme.colors.onBackground,
                    )
                    Text(
                        text = book.author,
                        style = TextStyles.Subtitle1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                    Text(
                        text = book.genre,
                        style = TextStyles.Subtitle1,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun ErrorComposable(state: BooksListFeature.State.Error) {
        AndroidView(factory = {
            ErrorStubWidget(it).apply {
                setMessage(state.message)
                visibility = View.VISIBLE
                setRetryClickListener {
                    feature.sendIntent(BooksListFeature.Intent.LoadBooks)
                }
            }
        })
    }

    @Composable
    @Preview
    fun BooksListScreenPreview() {
        BooksListScreen(
            state = BooksListFeature.State.Content(
                listOf(
                    StoreBook(
                        id = "1",
                        genre = "Classic",
                        author = "Pierre Cardine",
                        title = "Wonderful life",
                        lang = "English",
                        format = "fb",
                        cover = "https://m.media-amazon.com/images/I/81sG60wsNtL.jpg",
                        link = "https://www.amazon.co.uk/Wonderful-Life-Burgess-Nature-History/dp/0099273454",
                    )
                )
            )
        )
    }
}