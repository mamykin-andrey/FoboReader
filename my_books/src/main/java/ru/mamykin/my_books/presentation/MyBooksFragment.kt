package ru.mamykin.my_books.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_books.*
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.extension.isVisible
import ru.mamykin.core.extension.showSnackbar
import ru.mamykin.core.ui.BaseFragment
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.my_books.R
import ru.mamykin.my_books.presentation.list.MyBooksRecyclerAdapter

class MyBooksFragment : BaseFragment(R.layout.fragment_my_books) {

    companion object {

        fun newInstance() = MyBooksFragment()
    }

    private val viewModel: MyBooksViewModel by viewModel()
    private lateinit var adapter: MyBooksRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        adapter = MyBooksRecyclerAdapter(
                { viewModel.onEvent(MyBooksViewModel.Event.OnBookClicked(it)) },
                { viewModel.onEvent(MyBooksViewModel.Event.OnBookAboutClicked(it)) },
                { viewModel.onEvent(MyBooksViewModel.Event.OnBookShareClicked(it)) },
                { viewModel.onEvent(MyBooksViewModel.Event.OnBookRemoveClicked(it)) }
        )
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), false)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(
                context!!,
                menu!!,
                R.id.action_search,
                R.string.menu_search,
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.onEvent(MyBooksViewModel.Event.OnQueryTextChanged(newText!!))
                        return true
                    }
                }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_books_list, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.router = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSortName -> viewModel.onEvent(MyBooksViewModel.Event.OnSortBooksClicked(BookDao.SortOrder.BY_NAME))
            R.id.actionSortReaded -> viewModel.onEvent(MyBooksViewModel.Event.OnSortBooksClicked(BookDao.SortOrder.BY_READED))
            R.id.actionSortDate -> viewModel.onEvent(MyBooksViewModel.Event.OnSortBooksClicked(BookDao.SortOrder.BY_DATE))
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun initViewModel() {
        viewModel.router = MyBooksRouter(activity!!)
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