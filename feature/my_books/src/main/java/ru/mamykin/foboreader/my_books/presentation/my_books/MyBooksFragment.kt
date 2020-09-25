package ru.mamykin.foboreader.my_books.presentation.my_books

import android.os.Bundle
import android.view.Menu
import android.view.View
import kotlinx.android.synthetic.main.fragment_my_books.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.core.extension.getSearchView
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.extension.queryChanges
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.domain.my_books.SortOrder
import ru.mamykin.foboreader.my_books.presentation.my_books.list.MyBooksRecyclerAdapter

@ExperimentalCoroutinesApi
@FlowPreview
class MyBooksFragment : BaseFragment<MyBooksViewModel, ViewState, Effect>(
    R.layout.fragment_my_books
) {
    override val viewModel: MyBooksViewModel by viewModel()
    private val navigator: MyBooksNavigator by inject()
    private val adapter = MyBooksRecyclerAdapter(
        navigator::openBook,
        navigator::openBookDetails
    ) { viewModel.sendEvent(Event.RemoveBook(it)) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        rvMyBooks.adapter = adapter
        srlScanBooks.isEnabled = false
    }

    fun scanBooks() {
        viewModel.sendEvent(Event.ScanBooks)
    }

    private fun initToolbar() = toolbar.apply {
        title = getString(R.string.my_books_screen_title)
        navigationIcon = null
        inflateMenu(R.menu.menu_books_list)
        menu.findItem(R.id.actionSortName)
            .clicks()
            .onEach { viewModel.sendEvent(Event.SortBooks(SortOrder.ByName)) }
        menu.findItem(R.id.actionSortReaded)
            .clicks()
            .onEach { viewModel.sendEvent(Event.SortBooks(SortOrder.ByReaded)) }
        menu.findItem(R.id.actionSortDate)
            .clicks()
            .onEach { viewModel.sendEvent(Event.SortBooks(SortOrder.ByDate)) }
        initSearchView(menu)
    }

    private fun initSearchView(menu: Menu) = menu.getSearchView(R.id.action_search).apply {
        queryHint = getString(R.string.my_books_menu_search)
        queryChanges()
            .filterNotNull()
            .onEach { viewModel.sendEvent(Event.FilterBooks(it)) }
    }

    override fun showState(state: ViewState) {
        srlScanBooks.isRefreshing = state.isLoading
        adapter.changeData(state.books)
        showEmptyState(state.books.isEmpty())
    }

    private fun showEmptyState(show: Boolean) {
        vNoMyBooks.isVisible = show
        rvMyBooks.isVisible = !show
    }
}