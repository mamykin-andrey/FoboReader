package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.fragment_books_store.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.getSearchView
import ru.mamykin.foboreader.core.extension.queryChanges
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.presentation.list.BooksStoreRecyclerAdapter

@ExperimentalCoroutinesApi
@FlowPreview
class BooksStoreFragment : BaseFragment<BooksStoreViewModel, ViewState, Effect>(
    R.layout.fragment_books_store
) {
    override val viewModel: BooksStoreViewModel by viewModel()

    private val adapter = BooksStoreRecyclerAdapter { viewModel.sendEvent(Event.DownloadBook(it)) }
    private val navigator: Navigator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        srlRefresh.setOnRefreshListener { viewModel.sendEvent(Event.LoadBooks) }
        rvBooks.adapter = adapter
        initToolbar()
    }

    private fun initToolbar() {
        toolbar.apply {
            title = getString(R.string.books_store)
            navigationIcon = null
            inflateMenu(R.menu.menu_books_store)
            initSearchView(menu)
        }
    }

    private fun initSearchView(menu: Menu) = menu.getSearchView(R.id.action_search).apply {
        queryHint = getString(R.string.menu_search)
        queryChanges()
            .filterNotNull()
            .onEach { viewModel.sendEvent(Event.FilterBooks(it)) }
    }

    override fun showState(state: ViewState) {
        srlRefresh.isRefreshing = state.isLoading
        adapter.changeItems(state.books)
    }

    override fun takeEffect(effect: Effect) {
        when (effect) {
            is Effect.OpenMyBooksScreen -> navigator.openMyBooksScreen()
            is Effect.ShowSnackbar -> showSnackbar(effect.message)
        }
    }
}