package ru.mamykin.foboreader.ui.devicebooks

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_device_books.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.AndroidFile
import ru.mamykin.foboreader.di.modules.DeviceBooksModule
import ru.mamykin.foboreader.extension.isVisible
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksPresenter
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksView
import ru.mamykin.foboreader.ui.devicebooks.list.FilesRecyclerAdapter
import ru.mamykin.foboreader.ui.global.BaseFragment
import ru.mamykin.foboreader.ui.global.UiUtils
import javax.inject.Inject

/**
 * Страница с файлами на устройстве
 */
class DeviceBooksFragment : BaseFragment(), DeviceBooksView, SearchView.OnQueryTextListener {

    companion object {

        fun newInstance(): DeviceBooksFragment {
            val args = Bundle()
            val fragment = DeviceBooksFragment()
            fragment.arguments = args
            return fragment
        }
    }

    @Inject
    @InjectPresenter
    lateinit var presenter: DeviceBooksPresenter

    private lateinit var adapter: FilesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun injectDependencies() {
        val module = DeviceBooksModule(DeviceBooksRouter(activity!!))
        getAppComponent().getDeviceBooksComponent(module).inject(this)
    }

    @ProvidePresenter
    fun provideDeviceBooksPresenter() = presenter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_device_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FilesRecyclerAdapter(presenter::onFileClicked, presenter::onDirectoryClicked)
        UiUtils.setupRecyclerView(context!!, rvFiles!!, adapter, LinearLayoutManager(context), true)
        btnUpDir.setOnClickListener { presenter.onParentDirectoryClicked() }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_device_books, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context!!, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun showFiles(files: List<AndroidFile>) {
        adapter.changeData(files)
    }

    override fun showCurrentDir(currentDir: String) {
        tvCurrentDir.text = currentDir
    }

    override fun showPermissionMessage() {
        UiUtils.showToast(context!!, R.string.permission_denied)
    }

    override fun showUpDir(show: Boolean) {
        btnUpDir.isVisible = show
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        return false
    }
}