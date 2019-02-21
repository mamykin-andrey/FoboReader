package ru.mamykin.foboreader.ui.dropbox

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_dropbox_books.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.di.modules.DropboxBooksModule
import ru.mamykin.foboreader.entity.DropboxFile
import ru.mamykin.foboreader.extension.isVisible
import ru.mamykin.foboreader.presentation.dropbox.DropboxBooksPresenter
import ru.mamykin.foboreader.presentation.dropbox.DropboxView
import ru.mamykin.foboreader.ui.dropbox.list.DropboxRecyclerAdapter
import ru.mamykin.foboreader.ui.global.BaseFragment
import ru.mamykin.foboreader.ui.global.UiUtils
import javax.inject.Inject

/**
 * Страница с файлами Dropbox
 */
class DropboxBooksFragment : BaseFragment(), DropboxView, SearchView.OnQueryTextListener {

    companion object {

        fun newInstance(): DropboxBooksFragment = DropboxBooksFragment()
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: DropboxBooksPresenter

    private lateinit var adapter: DropboxRecyclerAdapter

    @ProvidePresenter
    fun providePresenter(): DropboxBooksPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun injectDependencies() {
        super.injectDependencies()
        val module = DropboxBooksModule(DropboxBooksRouter(activity!!))
        getAppComponent().getDropboxBooksComponent(module).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_dropbox_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DropboxRecyclerAdapter(
                presenter::onFileClicked,
                presenter::onDirectoryClicked,
                presenter::onParentDirectoryClicked
        )
        UiUtils.setupRecyclerView(context!!, rvBooks, adapter, LinearLayoutManager(context), true)

        btnLogin.setOnClickListener { presenter.onLoginClicked() }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_dropbox_books, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun showFiles(files: List<DropboxFile>) {
        adapter.hideLoadingItem()
        pbLoading.isVisible = false
        llNoAuth.isVisible = false
        rvBooks.isVisible = true
        adapter.changeData(files)
    }

    override fun showLoadingItem(position: Int?) {
        position?.let { adapter.showLoadingItem(it) } ?: adapter.hideLoadingItem()
    }

    override fun showLoading(show: Boolean) {
        pbLoading.isVisible = show
    }

    override fun showAuth() {
        llNoAuth.isVisible = true
        rvBooks.isVisible = false
    }

    override fun onQueryTextSubmit(query: String): Boolean = false

    override fun onQueryTextChange(newText: String): Boolean = false
}