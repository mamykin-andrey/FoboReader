package ru.mamykin.my_books.presentation.my_books

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_books.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.core.extension.isVisible
import ru.mamykin.core.extension.showSnackbar
import ru.mamykin.core.ui.BaseFragment
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.my_books.R
import ru.mamykin.my_books.domain.my_books.SortOrder
import ru.mamykin.my_books.presentation.my_books.list.MyBooksRecyclerAdapter

class MyBooksFragment : BaseFragment(R.layout.fragment_my_books) {

    private val viewModel: MyBooksViewModel by viewModel()
    private val adapter = MyBooksRecyclerAdapter { action, id ->
        viewModel.onBookAction(action, id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewModel()
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), false)
    }

    override fun loadData() {
        viewModel.loadBooks()
    }

    private fun initToolbar() {
        toolbar!!.title = getString(R.string.my_books)
        toolbar!!.inflateMenu(R.menu.menu_books_list)
        toolbar!!.setOnMenuItemClickListener(::onMenuItemClicked)
        UiUtils.setupSearchView(
                context!!,
                toolbar!!.menu,
                R.id.action_search,
                R.string.menu_search,
                viewModel::filterBooks
        )
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSortName -> viewModel.sortBooks(SortOrder.ByName)
            R.id.actionSortReaded -> viewModel.sortBooks(SortOrder.ByReaded)
            R.id.actionSortDate -> viewModel.sortBooks(SortOrder.ByDate)
            else -> return false
        }
        return true
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            state.books.takeIf { it.isNotEmpty() }?.let(adapter::changeData)
            showEmptyState(state.books.isEmpty())
            state.error?.let(::getString)?.let { showSnackbar(it) }
        })
    }

    private fun showEmptyState(show: Boolean) {
        vNoBooks.isVisible = show
        rvBooks.isVisible = !show
    }
}