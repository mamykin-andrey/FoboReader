package ru.mamykin.my_books.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_books.*
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.extension.isVisible
import ru.mamykin.core.extension.showSnackbar
import ru.mamykin.core.ui.BaseFragment
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.my_books.R
import ru.mamykin.my_books.di.DaggerMyBooksComponent
import ru.mamykin.my_books.di.MyBooksModule
import ru.mamykin.my_books.presentation.list.MyBooksRecyclerAdapter
import javax.inject.Inject

class MyBooksFragment : BaseFragment(R.layout.fragment_my_books) {

    companion object {
        fun newInstance() = MyBooksFragment()
    }

    @Inject
    lateinit var viewModel: MyBooksViewModel
    private lateinit var adapter: MyBooksRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDi()
    }

    private fun initDi() {
        DaggerMyBooksComponent.builder()
                .appComponent(getAppComponent())
                .myBooksModule(MyBooksModule(this))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initViewModel()
        adapter = MyBooksRecyclerAdapter(
                { viewModel.onEvent(MyBooksViewModel.Event.OnBookClicked(it)) },
                { viewModel.onEvent(MyBooksViewModel.Event.OnBookAboutClicked(it)) },
                { viewModel.onEvent(MyBooksViewModel.Event.OnBookShareClicked(it)) },
                { viewModel.onEvent(MyBooksViewModel.Event.OnBookRemoveClicked(it)) }
        )
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.router = null
    }

    private fun initToolbar() {
        toolbar!!.title = getString(R.string.my_books)
        toolbar!!.inflateMenu(R.menu.menu_books_list)
        toolbar!!.setOnMenuItemClickListener(::onMenuItemClicked)
//        UiUtils.setupSearchView(
//                context!!,
//                menu!!,
//                R.id.action_search,
//                R.string.menu_search,
//                object : SearchView.OnQueryTextListener {
//                    override fun onQueryTextSubmit(query: String?): Boolean {
//                        return false
//                    }
//
//                    override fun onQueryTextChange(newText: String?): Boolean {
//                        viewModel.onEvent(MyBooksViewModel.Event.OnQueryTextChanged(newText!!))
//                        return true
//                    }
//                }
//        )
    }

    private fun onMenuItemClicked(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSortName -> viewModel.onEvent(MyBooksViewModel.Event.OnSortBooksClicked(BookDao.SortOrder.BY_NAME))
            R.id.actionSortReaded -> viewModel.onEvent(MyBooksViewModel.Event.OnSortBooksClicked(BookDao.SortOrder.BY_READED))
            R.id.actionSortDate -> viewModel.onEvent(MyBooksViewModel.Event.OnSortBooksClicked(BookDao.SortOrder.BY_DATE))
            else -> return false
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