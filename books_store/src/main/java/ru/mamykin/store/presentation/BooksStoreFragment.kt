package ru.mamykin.store.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main_store.*
import ru.mamykin.core.extension.showSnackbar
import ru.mamykin.core.ui.BaseFragment
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.store.R
import ru.mamykin.store.presentation.list.BooksStoreRecyclerAdapter

class BooksStoreFragment : BaseFragment(R.layout.fragment_main_store) {

    companion object {
        fun newInstance(): BooksStoreFragment = BooksStoreFragment()
    }

    private val adapter = BooksStoreRecyclerAdapter()
//    private val viewModel: BooksStoreViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //srlRefresh.setOnRefreshListener { viewModel.loadBooks() }
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context))
        initToolbar()
        initViewModel()
    }

    private fun initToolbar() {
        toolbar!!.title = getString(R.string.books_store)
        toolbar!!.inflateMenu(R.menu.menu_books_store)
        toolbar!!.setOnMenuItemClickListener { true }
        // UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, object : SearchView.OnQueryTextListener {
        //            override fun onQueryTextSubmit(query: String?): Boolean {
        //                return false
        //            }
        //
        //            override fun onQueryTextChange(newText: String?): Boolean {
        //                return false
        //            }
        //        })
    }

    private fun initViewModel() {
//        viewModel.loadBooks()
//        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
//            srlRefresh.isRefreshing = state.isLoading
//            state.isError.takeIf { it }?.let { showSnackbar(R.string.books_store_load_error) }
//            state.books.takeIf { it.isNotEmpty() }?.let(adapter::changeItems)
//        })
    }
}