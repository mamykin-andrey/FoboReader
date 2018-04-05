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
    fun providePresenter(): MyBooksPresenter = presenter

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

        return inflater.inflate(R.layout.fragment_my_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BooksRecyclerAdapter(this)
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), false)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_books_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSortName -> presenter.onSortByNameSelected()
            R.id.actionSortReaded -> presenter.onSortByReadedSelected()
            R.id.actionSortDate -> presenter.onSortByDateSelected()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onBookClicked(bookPath: String) {
        presenter.onBookClicked(bookPath)
    }

    override fun onBookAboutClicked(bookPath: String) {
        presenter.onBookAboutClicked(bookPath)
    }

    override fun onBookShareClicked(bookPath: String) {
        presenter.onBookShareClicked(bookPath)
    }

    override fun onBookRemoveClicked(bookPath: String) {
        presenter.onBookRemoveClicked(bookPath)
    }

    override fun showBooks(books: List<FictionBook>) {
        showEmptyState(books.isEmpty())
        adapter.changeData(books)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        presenter.onQueryTextChange(newText)
        return true
    }

    private fun showEmptyState(show: Boolean) {
        vNoBooks.isVisible = show
        rvBooks.isVisible = !show
    }
}