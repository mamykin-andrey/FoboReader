package ru.mamykin.foboreader.store.presentation

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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.di.ComponentHolder
import ru.mamykin.foboreader.core.extension.apiHolder
import ru.mamykin.foboreader.core.extension.commonApi
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.presentation.BaseFragment
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.di.DaggerBookCategoriesComponent
import ru.mamykin.foboreader.store.domain.model.BookCategory
import ru.mamykin.foboreader.uikit.ErrorStubWidget
import ru.mamykin.foboreader.uikit.compose.FoboReaderTheme
import ru.mamykin.foboreader.uikit.compose.TextStyles
import javax.inject.Inject

class BookCategoriesFragment : BaseFragment() {

    companion object {
        fun newInstance(): Fragment = BookCategoriesFragment()
    }

    override val featureName: String = "book_categories"

    @Inject
    internal lateinit var feature: BookCategoriesFeature

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        ComponentHolder.getOrCreateComponent(featureName) {
            DaggerBookCategoriesComponent.factory().create(
                commonApi(),
                apiHolder().networkApi(),
                apiHolder().navigationApi(),
                apiHolder().settingsApi(),
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
            BookCategoriesScreen(state = feature.state)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeFeature()
    }

    private fun observeFeature() {
        feature.effectFlow.collectWithRepeatOnStarted(::takeEffect)
    }

    private fun takeEffect(effect: BookCategoriesFeature.Effect) {
        when (effect) {
            is BookCategoriesFeature.Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }

    @Composable
    internal fun BookCategoriesScreen(state: BookCategoriesFeature.State) {
        FoboReaderTheme {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(id = R.string.books_store_title))
                    }, elevation = 12.dp
                )
            }, content = {
                when (state) {
                    is BookCategoriesFeature.State.Progress -> LoadingComposable()
                    is BookCategoriesFeature.State.Error -> ErrorComposable(state)
                    is BookCategoriesFeature.State.Content -> ContentComposable(state)
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
            CircularProgressIndicator()
        }
    }

    @Composable
    private fun ContentComposable(state: BookCategoriesFeature.State.Content) {
        Column {
            state.categories.forEach { CategoryComposable(it) }
        }
    }

    @Composable
    private fun CategoryComposable(category: BookCategory) {
        // TODO: theming
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .clickable {
                    feature.sendIntent(BookCategoriesFeature.Intent.OpenCategory(category.id))
                },
            elevation = 16.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = category.name,
                        style = TextStyles.Subtitle1,
                        color = MaterialTheme.colors.onBackground,
                    )
                    if (category.description != null) {
                        Text(
                            text = category.description,
                            style = TextStyles.Body2,
                            color = MaterialTheme.colors.onBackground,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.books_store_category_count, category.booksCount),
                        style = TextStyles.Body2,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding()
                )
            }
        }
    }

    @Composable
    private fun ErrorComposable(state: BookCategoriesFeature.State.Error) {
        AndroidView(factory = {
            ErrorStubWidget(it).apply {
                setMessage(state.errorMessage)
                visibility = View.VISIBLE
                setRetryClickListener {
                    feature.sendIntent(BookCategoriesFeature.Intent.LoadCategories)
                }
            }
        })
    }

    @Preview
    @Composable
    fun Preview() {
        BookCategoriesScreen(
            state = BookCategoriesFeature.State.Error(
                "Unable to parse incoming data: root is not an object!"
            )
        )
    }
}