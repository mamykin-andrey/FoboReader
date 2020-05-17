package ru.mamykin.foboreader.my_books.presentation.my_books

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_books.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import reactivecircus.flowbinding.android.view.clicks
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.platform.Navigator
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.domain.my_books.SortOrder
import ru.mamykin.foboreader.my_books.presentation.my_books.list.MyBooksRecyclerAdapter

@ExperimentalCoroutinesApi
@FlowPreview
class MyBooksFragment : BaseFragment(R.layout.fragment_my_books) {

    private val viewModel: MyBooksViewModel by viewModel()
    private val navigator: Navigator by inject()
    private val adapter = MyBooksRecyclerAdapter(
            navigator::openBook,
            navigator::openBookDetails
    ) { viewModel.removeBook(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewModel()
        UiUtils.setupRecyclerView(context!!, rvMyBooks, adapter, LinearLayoutManager(context), false)
        srlScanBooks.isEnabled = false
    }

    override fun loadData() {
        viewModel.loadBooks()
    }

    fun scanBooks() {
        viewModel.scanBooks()
    }

    private fun initToolbar() = toolbar!!.apply {
        title = getString(R.string.my_books)
        inflateMenu(R.menu.menu_books_list)
        menu.findItem(R.id.actionSortName)
                .clicks()
                .onEach { viewModel.sortBooks(SortOrder.ByName) }
        menu.findItem(R.id.actionSortReaded)
                .clicks()
                .onEach { viewModel.sortBooks(SortOrder.ByReaded) }
        menu.findItem(R.id.actionSortDate)
                .clicks()
                .onEach { viewModel.sortBooks(SortOrder.ByDate) }
        UiUtils.setupSearchView(
                context!!,
                menu,
                R.id.action_search,
                R.string.menu_search,
                { viewModel.filterBooks(it) }
        )
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            srlScanBooks.isRefreshing = state.isLoading
            adapter.changeData(state.books)
            showEmptyState(state.books.isEmpty())
            state.error?.let { showSnackbar(it) }
        })
    }

    private fun showEmptyState(show: Boolean) {
        vNoMyBooks.isVisible = show
        rvMyBooks.isVisible = !show
    }
}