package ru.mamykin.foboreader.book_details.details

import android.annotation.SuppressLint
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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import coil.compose.AsyncImage
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.navigation.ScreenProvider
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles
import javax.inject.Inject

class BookDetailsFragment : BaseFragment() {

    companion object {

        private const val EXTRA_BOOK_ID = "extra_book_id"

        fun newInstance(bookId: Long): Fragment = BookDetailsFragment().apply {
            arguments = Bundle(1).apply {
                putLong(EXTRA_BOOK_ID, bookId)
            }
        }
    }

    override val featureName: String = "book_details"

    @Inject
    internal lateinit var feature: BookDetailsFeature

    @Inject
    internal lateinit var router: Router

    @Inject
    internal lateinit var screenProvider: ScreenProvider

    private val bookId by lazy { requireArguments().getLong(EXTRA_BOOK_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFeature()
    }

    private fun initFeature() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerBookDetailsComponent.factory().create(
                bookId,
                apiHolder().navigationApi(),
                commonApi(),
            )
        }.inject(this)
        feature.sendIntent(BookDetailsFeature.Intent.LoadBookInfo)
    }

    override fun onCleared() {
        feature.onCleared()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            BookDetailsScreen(feature.state, feature.effectFlow)
        }
    }

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    @Composable
    private fun BookDetailsScreen(state: BookDetailsFeature.State, effectFlow: Flow<BookDetailsFeature.Effect>) {
        LaunchedEffect(effectFlow) {
            effectFlow.collect {
                takeEffect(it)
            }
        }

        FoboReaderTheme {
            Scaffold(topBar = {
                TopAppBar(title = {
                    Text(text = stringResource(id = R.string.my_books_book_info_title))
                }, elevation = 12.dp, navigationIcon = {
                    IconButton(onClick = {
                        requireActivity().onBackPressed()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            modifier = Modifier,
                            contentDescription = null,
                        )
                    }
                })
            }, content = {
                when (state) {
                    is BookDetailsFeature.State.Loading -> LoadingComposable()
                    is BookDetailsFeature.State.Loaded -> LoadedComposable(state)
                }
            })
        }
    }

    private fun takeEffect(effect: BookDetailsFeature.Effect) = when (effect) {
        is BookDetailsFeature.Effect.NavigateToReadBook -> {
            router.navigateTo(screenProvider.readBookScreen(bookId))
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
    private fun LoadedComposable(state: BookDetailsFeature.State.Loaded) {
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
                            color = MaterialTheme.colors.onBackground
                        )
                        Text(text = state.bookDetails.author)
                    }
                }
                FloatingActionButton(
                    onClick = {
                        feature.sendIntent(BookDetailsFeature.Intent.OpenBook)
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
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            )
            Text(
                text = stringResource(R.string.my_books_no_bookmarks),
                style = TextStyles.Body2,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            )

            Text(
                text = stringResource(R.string.my_books_book_path),
                style = TextStyles.Subtitle1,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            )
            Text(
                text = state.bookDetails.filePath,
                style = TextStyles.Body2,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            )

            Text(
                text = stringResource(R.string.my_books_current_page),
                style = TextStyles.Subtitle1,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            )
            Text(
                text = state.bookDetails.currentPage.toString(),
                style = TextStyles.Body2,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            )

            Text(
                text = stringResource(R.string.my_books_book_genre),
                style = TextStyles.Subtitle1,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 12.dp, start = 16.dp, end = 16.dp)
            )
            Text(
                text = state.bookDetails.genre,
                style = TextStyles.Body2,
                color = MaterialTheme.colors.onBackground,
                modifier = Modifier
                    .padding(top = 8.dp, start = 16.dp, end = 16.dp)
            )
        }
    }

    @Preview
    @Composable
    private fun MyBooksScreenPreview() {
        BookDetailsScreen(
            BookDetailsFeature.State.Loaded(
                BookDetails(
                    "Author",
                    "Title",
                    "https://m.media-amazon.com/images/I/41urypNXYyL.jpg",
                    "/dev/null",
                    10,
                    "Genre"
                )
            ), emptyFlow()
        )
    }
}