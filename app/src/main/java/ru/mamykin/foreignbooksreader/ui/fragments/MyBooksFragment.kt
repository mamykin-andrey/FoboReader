package ru.mamykin.foreignbooksreader.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter

import butterknife.BindView
import butterknife.ButterKnife
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.models.FictionBook
import ru.mamykin.foreignbooksreader.presenters.MyBooksPresenter
import ru.mamykin.foreignbooksreader.ui.activities.BookDetailsActivity
import ru.mamykin.foreignbooksreader.ui.activities.ReadBookActivity
import ru.mamykin.foreignbooksreader.ui.adapters.BooksRecyclerAdapter
import ru.mamykin.foreignbooksreader.views.MyBooksView

/**
 * Страница с книгами пользователя
 */
class MyBooksFragment : MvpAppCompatFragment(), SearchView.OnQueryTextListener, MyBooksView, BooksRecyclerAdapter.OnBookClickListener {
    @BindView(R.id.rvBooks)
    protected var rvBooks: RecyclerView? = null
    @BindView(R.id.vNoBooks)
    protected var vNoBooks: View? = null

    private var adapter: BooksRecyclerAdapter? = null

    @InjectPresenter
    internal var presenter: MyBooksPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_my_books, container, false)
        ButterKnife.bind(this, contentView)

        adapter = BooksRecyclerAdapter(this)
        UiUtils.setupRecyclerView(context, rvBooks!!,
                adapter!!, LinearLayoutManager(context), false)

        return contentView
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_books_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.actionSortName -> {
                presenter!!.onActionSortNameSelected()
                return true
            }
            R.id.actionSortReaded -> {
                presenter!!.onActionSortReadedSelected()
                return true
            }
            R.id.actionSortDate -> {
                presenter!!.onActionSortDateSelected()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBookClicked(position: Int) {
        presenter!!.onBookClicked(position)
    }

    override fun onBookAboutClicked(position: Int) {
        presenter!!.onBookAboutClicked(position)
    }

    override fun onBookShareClicked(position: Int) {
        presenter!!.onBookShareClicked(position)
    }

    override fun onBookRemoveClicked(position: Int) {
        presenter!!.onBookRemoveClicked(position)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        presenter!!.onQueryTextChange(newText)
        return true
    }

    override fun showEmptyStateView() {
        vNoBooks!!.visibility = View.VISIBLE
        rvBooks!!.visibility = View.GONE
    }

    override fun showBooksList(booksList: List<FictionBook>) {
        vNoBooks!!.visibility = View.GONE
        rvBooks!!.visibility = View.VISIBLE
        adapter!!.changeData(booksList)
    }

    override fun openBook(bookId: Int) {
        startActivity(ReadBookActivity.getStartIntent(context, bookId))
    }

    override fun openBookDetails(bookId: Int) {
        startActivity(BookDetailsActivity.getStartIntent(context, bookId))
    }

    override fun showBookShareDialog(title: String, url: String) {
        showBookShareDialog(getString(R.string.download_on, title, url))
    }

    override fun showBookShareDialog(title: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, title)
        shareIntent.type = "text/plain"
        startActivity(shareIntent)
    }

    companion object {

        fun newInstance(): MyBooksFragment {
            val args = Bundle()
            val fragment = MyBooksFragment()
            fragment.arguments = args
            return fragment
        }
    }
}