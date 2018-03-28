package ru.mamykin.foboreader.ui.dropbox

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.dropbox.core.android.Auth
import kotlinx.android.synthetic.main.fragment_dropbox_books.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.data.model.DropboxFile
import ru.mamykin.foboreader.presentation.dropbox.DropboxBooksPresenter
import ru.mamykin.foboreader.presentation.dropbox.DropboxView
import ru.mamykin.foboreader.ui.dropbox.list.DropboxRecyclerAdapter
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity

/**
 * Страница с файлами Dropbox
 */
class DropboxBooksFragment : MvpAppCompatFragment(), DropboxView, SearchView.OnQueryTextListener {

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

    @InjectPresenter
    lateinit var presenter: DropboxBooksPresenter

    private lateinit var adapter: DropboxRecyclerAdapter

    @ProvidePresenter
    internal fun providePresenter(): DropboxBooksPresenter {
        return DropboxBooksPresenter(arguments.getString(CURRENT_DIR_EXTRA))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_dropbox_books, container, false)

        adapter = DropboxRecyclerAdapter(presenter::onFileClicked, presenter::onDirectoryClicked)
        UiUtils.setupRecyclerView(context, rvBooks, adapter, LinearLayoutManager(context), true)

        btnLogin.setOnClickListener { presenter.onLoginClicked() }
        ibUp.setOnClickListener { presenter.onParentDirectoryClicked() }

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
        rvBooks.visibility = View.VISIBLE
        adapter.changeData(filesList)
    }

    override fun hideFiles() {
        rvBooks.visibility = View.GONE
    }

    override fun showCurrentDir(dir: String) {
        tvCurrentDir.text = dir
    }

    override fun openBook(path: String) {
        startActivity(ReadBookActivity.getStartIntent(context, path))
    }

    override fun showLoadingItem(position: Int) {
        adapter.showLoadingItem(position)
    }

    override fun hideLoadingItem() {
        adapter.hideLoadingItem()
    }

    override fun showLoading() {
        pbLoading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pbLoading.visibility = View.GONE
    }

    override fun showAuth() {
        llNoAuth.visibility = View.VISIBLE
    }

    override fun hideAuth() {
        llNoAuth.visibility = View.GONE
    }

    override fun showUpButton(show: Boolean) {
        UiUtils.setVisibility(ibUp, show)
    }

    override fun startOAuth2Authentication() {
        Auth.startOAuth2Authentication(context, context.getString(R.string.dropbox_api_key))
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }
}