package ru.mamykin.foboreader.ui.store

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

import butterknife.BindView
import butterknife.ButterKnife
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.data.model.StoreBook
import ru.mamykin.foboreader.presentation.store.StorePresenter
import ru.mamykin.foboreader.ui.store.list.BooksStoreRecyclerAdapter
import ru.mamykin.foboreader.presentation.store.BooksStoreView

/**
 * Страница с магазином книг
 */
class BooksStoreFragment : MvpAppCompatFragment(), BooksStoreView, SearchView.OnQueryTextListener {
    @InjectPresenter
    internal var presenter: StorePresenter? = null

    private var adapter: BooksStoreRecyclerAdapter? = null

    @BindView(R.id.rvBooks)
    protected var rvBooks: RecyclerView? = null
    @BindView(R.id.srlRefresh)
    protected var srlRefresh: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_main_store, container, false)
        ButterKnife.bind(this, contentView)
        srlRefresh!!.setOnRefreshListener { presenter!!.loadStoreCategories() }

        adapter = BooksStoreRecyclerAdapter()
        UiUtils.setupRecyclerView(context, rvBooks!!,
                adapter!!, LinearLayoutManager(context), false)

        initCategoriesRecyclerView()
        return contentView
    }

    private fun initCategoriesRecyclerView() {}

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_books_store, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return super.onOptionsItemSelected(item)
    }

    override fun showBooks(booksList: List<StoreBook>) {
        adapter!!.changeData(booksList)
    }

    override fun showMessage(message: String) {
        UiUtils.showToast(context, message, Toast.LENGTH_SHORT)
    }

    override fun showLoading(show: Boolean) {
        srlRefresh!!.isRefreshing = show
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    companion object {

        fun newInstance(): BooksStoreFragment {
            val args = Bundle()
            val fragment = BooksStoreFragment()
            fragment.arguments = args
            return fragment
        }
    }
}