package ru.mamykin.foboreader.store.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_books_store.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.presentation.list.BooksStoreRecyclerAdapter

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

    private fun initToolbar() {
        toolbar!!.title = getString(R.string.books_store)
        toolbar!!.inflateMenu(R.menu.menu_books_store)
        toolbar!!.setOnMenuItemClickListener { true }
    }

    private fun initViewModel() {
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            srlRefresh.isRefreshing = state.isLoading
            if (state.isError) showSnackbar(R.string.books_store_load_error)
            state.books.takeIf { it.isNotEmpty() }?.let(adapter::changeItems)
        })
    }

    private fun showSnackbar(msgId: Int) {
        Toast.makeText(context, getString(msgId), Toast.LENGTH_SHORT).show()
    }
}