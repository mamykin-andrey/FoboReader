package ru.mamykin.store.presentation

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_main_store.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.mamykin.core.extension.showSnackbar
import ru.mamykin.core.platform.Router
import ru.mamykin.core.ui.BaseFragment
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.store.R
import ru.mamykin.store.presentation.list.BooksStoreRecyclerAdapter

class BooksStoreFragment : BaseFragment(R.layout.fragment_main_store) {

    private val adapter = BooksStoreRecyclerAdapter { viewModel.downloadBook(it) }
    private val viewModel: BooksStoreViewModel by viewModel()
    private val router: Router by inject { parametersOf(activity) }

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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.router = null
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
        viewModel.router = router
    }
}