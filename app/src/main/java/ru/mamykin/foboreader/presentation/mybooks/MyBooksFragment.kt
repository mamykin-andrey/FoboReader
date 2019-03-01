package ru.mamykin.foboreader.presentation.mybooks

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_my_books.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.domain.entity.FictionBook
import ru.mamykin.foboreader.presentation.mybooks.list.BooksRecyclerAdapter

/**
 * Страница с книгами пользователя
 */
class MyBooksFragment : BaseFragment(), MyBooksView, SearchView.OnQueryTextListener {

    companion object {

        fun newInstance() = MyBooksFragment()
    }

    override val layoutId: Int = R.layout.fragment_my_books

    @InjectPresenter
    lateinit var presenter: MyBooksPresenter

    private lateinit var adapter: BooksRecyclerAdapter

    @ProvidePresenter
    fun providePresenter(): MyBooksPresenter = getAppComponent()
            .getMyBooksComponent()
            .getMyBooksPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BooksRecyclerAdapter(
                presenter::onBookClicked,
                presenter::onBookAboutClicked,
                presenter::onBookShareClicked,
                presenter::onBookRemoveClicked
        )
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), false)
        presenter.router = MyBooksRouter(activity!!)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_books_list, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.router = null
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

    override fun showBooks(books: List<FictionBook>) {
        showEmptyState(books.isEmpty())
        adapter.changeData(books)
    }

    private fun showEmptyState(show: Boolean) {
        vNoBooks.isVisible = show
        rvBooks.isVisible = !show
    }

    override fun onQueryTextSubmit(query: String) = false

    override fun onQueryTextChange(newText: String): Boolean {
        presenter.onQueryTextChange(newText)
        return true
    }
}