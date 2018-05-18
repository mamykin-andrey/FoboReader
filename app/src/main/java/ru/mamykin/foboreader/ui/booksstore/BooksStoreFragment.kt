package ru.mamykin.foboreader.ui.booksstore

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_main_store.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.StoreBook
import ru.mamykin.foboreader.presentation.booksstore.BooksStorePresenter
import ru.mamykin.foboreader.presentation.booksstore.BooksStoreView
import ru.mamykin.foboreader.ui.global.BaseFragment
import ru.mamykin.foboreader.ui.global.UiUtils
import ru.mamykin.foboreader.ui.booksstore.list.BooksStoreRecyclerAdapter
import javax.inject.Inject

/**
 * Страница с магазином книг
 */
class BooksStoreFragment : BaseFragment(), BooksStoreView, SearchView.OnQueryTextListener {

    companion object {

        fun newInstance(): BooksStoreFragment {
            val args = Bundle()
            val fragment = BooksStoreFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: BooksStorePresenter

    private lateinit var adapter: BooksStoreRecyclerAdapter

    @ProvidePresenter
    fun providePresenter(): BooksStorePresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun injectDependencies() {
        super.injectDependencies()
        getAppComponent().getBooksStoreComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_main_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        srlRefresh.setOnRefreshListener { presenter.loadBooks() }

        adapter = BooksStoreRecyclerAdapter()
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), false)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_books_store, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun showBooks(booksList: List<StoreBook>) {
        adapter.changeData(booksList)
    }

    override fun showMessage(message: String) {
        UiUtils.showToast(context!!, message, Toast.LENGTH_SHORT)
    }

    override fun showLoading(show: Boolean) {
        srlRefresh.isRefreshing = show
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }
}