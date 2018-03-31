package ru.mamykin.foboreader.ui.devicebooks

import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_device_books.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.data.model.AndroidFile
import ru.mamykin.foboreader.extension.isVisible
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksPresenter
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksView
import ru.mamykin.foboreader.ui.devicebooks.list.FilesRecyclerAdapter
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity
import javax.inject.Inject

/**
 * Страница с файлами на устройстве
 */
class DeviceBooksFragment : MvpAppCompatFragment(), DeviceBooksView, SearchView.OnQueryTextListener {

    companion object {
        private const val CURRENT_DIR_EXTRA = "current_dir_extra"

        fun newInstance(currentDir: String?): DeviceBooksFragment {
            val args = Bundle()
            args.putString(CURRENT_DIR_EXTRA, currentDir
                    ?: Environment.getExternalStorageDirectory().toString())
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

    @ProvidePresenter
    fun provideDeviceBooksPresenter(): DeviceBooksPresenter {
        return presenter
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val contentView = inflater!!.inflate(R.layout.fragment_device_books, container, false)

        adapter = FilesRecyclerAdapter(presenter::onFileClicked, presenter::onDirectoryClicked)
        UiUtils.setupRecyclerView(context, rvFiles!!, adapter, LinearLayoutManager(context), true)
        btnUpDir.setOnClickListener { presenter.onUpDirClicked() }

        return contentView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_device_books, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(context, menu!!, R.id.action_search, R.string.menu_search, this)
    }

    override fun showFiles(files: List<AndroidFile>) {
        adapter.changeData(files)
    }

    override fun showCurrentDir(currentDir: String) {
        tvCurrentDir.text = currentDir
    }

    override fun showPermissionMessage() {
        UiUtils.showToast(context, R.string.permission_denied)
    }

    override fun openBook(path: String) {
        startActivity(ReadBookActivity.getStartIntent(context, path))
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