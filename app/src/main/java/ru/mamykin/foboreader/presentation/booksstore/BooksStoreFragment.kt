package ru.mamykin.foboreader.presentation.booksstore

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_main_store.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.presentation.booksstore.list.BooksStoreRecyclerAdapter

/**
 * Страница с магазином книг
 */
class BooksStoreFragment : BaseFragment(), BooksStoreView, SearchView.OnQueryTextListener {

    companion object {

        fun newInstance(): BooksStoreFragment = BooksStoreFragment()
    }

    override val layoutId: Int = R.layout.fragment_main_store

    @InjectPresenter
    lateinit var presenter: BooksStorePresenter

    private lateinit var adapter: BooksStoreRecyclerAdapter

    @ProvidePresenter
    fun providePresenter(): BooksStorePresenter = getAppComponent()
            .getBooksStoreComponent()
            .getBooksStorePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        srlRefresh.setOnRefreshListener { presenter.loadBooks() }
        adapter = BooksStoreRecyclerAdapter()
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_books_store, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun showStoreBooks(books: List<Any>) {
        adapter.changeItems(books)
    }

    override fun showMessage(message: String) {
        showToast(message)
    }

    override fun showLoading(show: Boolean) {
        srlRefresh.isRefreshing = show
    }

    override fun onQueryTextSubmit(query: String): Boolean = false

    override fun onQueryTextChange(newText: String): Boolean = false
}