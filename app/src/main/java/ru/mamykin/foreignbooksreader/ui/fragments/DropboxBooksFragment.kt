package ru.mamykin.foreignbooksreader.ui.fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import ru.mamykin.foreignbooksreader.R
import ru.mamykin.foreignbooksreader.common.UiUtils
import ru.mamykin.foreignbooksreader.models.DropboxFile
import ru.mamykin.foreignbooksreader.presenters.DropboxBooksPresenter
import ru.mamykin.foreignbooksreader.ui.activities.ReadBookActivity
import ru.mamykin.foreignbooksreader.ui.adapters.DropboxRecyclerAdapter
import ru.mamykin.foreignbooksreader.views.DropboxView

/**
 * Страница с файлами Dropbox
 */
class DropboxBooksFragment : MvpAppCompatFragment(), DropboxView, SearchView.OnQueryTextListener {

    @BindView(R.id.tvCurrentDir)
    protected var tvCurrentDir: TextView? = null
    @BindView(R.id.pbLoading)
    protected var pbLoading: View? = null
    @BindView(R.id.llFiles)
    protected var llFiles: View? = null
    @BindView(R.id.llNoAuth)
    protected var llNoAuth: View? = null
    @BindView(R.id.rvBooks)
    protected var rvBooks: RecyclerView? = null
    @BindView(R.id.btnLogin)
    protected var btnLogin: View? = null
    @BindView(R.id.ibUp)
    protected var btnUpDir: View? = null

    @InjectPresenter
    internal var presenter: DropboxBooksPresenter? = null

    private var adapter: DropboxRecyclerAdapter? = null

    @ProvidePresenter
    internal fun provideDropboxBooksPresenter(): DropboxBooksPresenter {
        return DropboxBooksPresenter(arguments.getString(CURRENT_DIR_EXTRA))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_dropbox_books, container, false)
        ButterKnife.bind(this, contentView)

        adapter = DropboxRecyclerAdapter({ position -> presenter!!.onItemClicked(position, adapter!!.getItem(position)) })
        UiUtils.setupRecyclerView(context, rvBooks!!, adapter!!, LinearLayoutManager(context), true)

        return contentView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_dropbox_books, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun showFiles(filesList: List<DropboxFile>) {
        rvBooks!!.visibility = View.VISIBLE
        adapter!!.changeData(filesList)
    }

    override fun hideFiles() {
        rvBooks!!.visibility = View.GONE
    }

    override fun showCurrentDir(currentDir: String) {
        tvCurrentDir!!.text = currentDir
    }

    override fun openBook(path: String) {
        startActivity(ReadBookActivity.getStartIntent(context, path))
    }

    override fun showLoadingItem(position: Int) {
        adapter!!.showLoadingItem(position)
    }

    override fun hideLoadingItem() {
        adapter!!.hideLoadingItem()
    }

    override fun showLoading() {
        pbLoading!!.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading!!.visibility = View.GONE
    }

    override fun showAuth() {
        llNoAuth!!.visibility = View.VISIBLE
    }

    override fun hideAuth() {
        llNoAuth!!.visibility = View.GONE
    }

    override fun showUpButton(show: Boolean) {
        UiUtils.setVisibility(btnUpDir!!, show)
    }

    @OnClick(R.id.btnLogin)
    fun onLoginClicked() {
        presenter!!.onLoginClicked()
    }

    @OnClick(R.id.ibUp)
    fun onUpClicked() {
        presenter!!.onUpClicked()
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }

    companion object {
        private val CURRENT_DIR_EXTRA = "url_path_extra"

        fun newInstance(currentDir: String?): DropboxBooksFragment {
            val args = Bundle()
            args.putString(CURRENT_DIR_EXTRA, currentDir ?: "")
            val fragment = DropboxBooksFragment()
            fragment.arguments = args
            return fragment
        }
    }
}