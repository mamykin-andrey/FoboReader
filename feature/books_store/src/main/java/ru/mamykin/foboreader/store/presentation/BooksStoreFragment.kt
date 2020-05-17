package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_books_store.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.extension.showToast
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.presentation.list.BooksStoreRecyclerAdapter

@ExperimentalCoroutinesApi
@FlowPreview
class BooksStoreFragment : BaseFragment(R.layout.fragment_books_store) {

    private val adapter = BooksStoreRecyclerAdapter { viewModel.downloadBook(it) }
    private val viewModel: BooksStoreViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        srlRefresh.setOnRefreshListener { viewModel.loadBooks() }
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context))
        initToolbar()
        initViewModel()
    }

    override fun loadData() {
        viewModel.loadBooks()
    }

    private fun initToolbar() = toolbar!!.apply {
        title = getString(R.string.books_store)
        inflateMenu(R.menu.menu_books_store)
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
            srlRefresh.isRefreshing = state.isLoading
            if (state.isError) context?.showToast(R.string.books_store_load_error)
            adapter.changeItems(state.books)
        })
    }
}