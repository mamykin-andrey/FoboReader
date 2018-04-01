package ru.mamykin.foboreader.ui.mybooks

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_my_books.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.di.modules.MyBooksModule
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.extension.isVisible
import ru.mamykin.foboreader.presentation.mybooks.MyBooksPresenter
import ru.mamykin.foboreader.presentation.mybooks.MyBooksView
import ru.mamykin.foboreader.ui.global.BaseFragment
import ru.mamykin.foboreader.ui.global.UiUtils
import ru.mamykin.foboreader.ui.mybooks.list.BooksRecyclerAdapter
import javax.inject.Inject

/**
 * Страница с книгами пользователя
 */
class MyBooksFragment : BaseFragment(), MyBooksView,
        SearchView.OnQueryTextListener, BooksRecyclerAdapter.OnBookClickListener {

    companion object {

        fun newInstance(): MyBooksFragment {
            val args = Bundle()
            val fragment = MyBooksFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: MyBooksPresenter

    private lateinit var adapter: BooksRecyclerAdapter

    @ProvidePresenter
    fun providePresenter() = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun injectDependencies() {
        val module = MyBooksModule(MyBooksRouter(activity!!))
        getAppComponent().getMyBooksComponent(module).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val contentView = inflater.inflate(R.layout.fragment_my_books, container, false)

        adapter = BooksRecyclerAdapter(this)
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), false)

        return contentView
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_books_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.actionSortName -> {
                presenter.onSortByNameSelected()
                return true
            }
            R.id.actionSortReaded -> {
                presenter.onSortByReadedSelected()
                return true
            }
            R.id.actionSortDate -> {
                presenter.onSortByDateSelected()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBookClicked(position: Int) {
        presenter.onBookClicked(position)
    }

    override fun onBookAboutClicked(position: Int) {
        presenter.onBookAboutClicked(position)
    }

    override fun onBookShareClicked(position: Int) {
        presenter.onBookShareClicked(position)
    }

    override fun onBookRemoveClicked(position: Int) {
        presenter.onBookRemoveClicked(position)
    }

    override fun showEmptyStateView(show: Boolean) {
        vNoBooks.isVisible = show
        rvBooks.isVisible = !show
    }

    override fun showBooksList(booksList: List<FictionBook>) {
        showEmptyStateView(false)
        adapter.changeData(booksList)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        presenter.onQueryTextChange(newText)
        return true
    }
}