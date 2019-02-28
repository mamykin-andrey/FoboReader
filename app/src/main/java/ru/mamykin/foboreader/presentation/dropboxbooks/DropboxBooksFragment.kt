package ru.mamykin.foboreader.presentation.dropboxbooks

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_dropbox_books.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.domain.entity.DropboxFile
import ru.mamykin.foboreader.presentation.dropboxbooks.list.DropboxRecyclerAdapter

/**
 * Страница с файлами Dropbox
 */
class DropboxBooksFragment : BaseFragment(), DropboxView, SearchView.OnQueryTextListener {

    companion object {

        fun newInstance(): DropboxBooksFragment = DropboxBooksFragment()
    }

    override val layoutId: Int = R.layout.fragment_dropbox_books

    @InjectPresenter
    lateinit var presenter: DropboxBooksPresenter

    private lateinit var adapter: DropboxRecyclerAdapter

    @ProvidePresenter
    fun providePresenter(): DropboxBooksPresenter = getAppComponent()
            .getDropboxBooksComponent()
            .getDropboxBooksPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DropboxRecyclerAdapter(presenter::onFileClicked, presenter::onDirectoryClicked)
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), true)
        btnLogin.setOnClickListener { presenter.onLoginClicked() }
        presenter.router = DropboxBooksRouter(activity!!)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_dropbox_books, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.router = null
    }

    override fun showFiles(files: List<DropboxFile>) {
        adapter.changeData(files)
        pbLoading.isVisible = false
        llNoAuth.isVisible = false
        rvBooks.isVisible = true
    }

    override fun showLoadingItem(position: Int) {
        //adapter.showLoadingItem(position)
    }

    override fun hideLoadingItem() {
        //adapter.hideLoadingItem()
    }

    override fun showLoading(show: Boolean) {
        pbLoading.isVisible = show
    }

    override fun showAuth() {
        llNoAuth.isVisible = true
        rvBooks.isVisible = false
    }

    override fun onError(message: String) {
        showSnackbar(message)
    }

    override fun onQueryTextSubmit(query: String): Boolean = false

    override fun onQueryTextChange(newText: String): Boolean = false
}