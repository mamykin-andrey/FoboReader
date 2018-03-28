package ru.mamykin.foboreader.ui.mybooks

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import kotlinx.android.synthetic.main.fragment_my_books.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.data.model.FictionBook
import ru.mamykin.foboreader.extension.isVisible
import ru.mamykin.foboreader.presentation.mybooks.MyBooksPresenter
import ru.mamykin.foboreader.presentation.mybooks.MyBooksView
import ru.mamykin.foboreader.ui.bookdetails.BookDetailsActivity
import ru.mamykin.foboreader.ui.mybooks.list.BooksRecyclerAdapter
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity

/**
 * Страница с книгами пользователя
 */
class MyBooksFragment : MvpAppCompatFragment(), SearchView.OnQueryTextListener,
        MyBooksView, BooksRecyclerAdapter.OnBookClickListener {

    companion object {

        fun newInstance(): MyBooksFragment {
            val args = Bundle()
            val fragment = MyBooksFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @InjectPresenter
    lateinit var presenter: MyBooksPresenter

    private lateinit var adapter: BooksRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_my_books, container, false)

        adapter = BooksRecyclerAdapter(this)
        UiUtils.setupRecyclerView(context, rvBooks, adapter, LinearLayoutManager(context), false)

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
                presenter.onActionSortNameSelected()
                return true
            }
            R.id.actionSortReaded -> {
                presenter.onActionSortReadedSelected()
                return true
            }
            R.id.actionSortDate -> {
                presenter.onActionSortDateSelected()
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

    override fun openBook(bookId: Int) {
        startActivity(ReadBookActivity.getStartIntent(context, bookId))
    }

    override fun openBookDetails(bookId: Int) {
        startActivity(BookDetailsActivity.getStartIntent(context, bookId))
    }

    override fun showBookShareDialog(bookName: String, url: String) {
        showBookShareDialog(getString(R.string.download_on, bookName, url))
    }

    override fun showBookShareDialog(bookName: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, bookName)
        shareIntent.type = "text/plain"
        startActivity(shareIntent)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        presenter.onQueryTextChange(newText)
        return true
    }
}