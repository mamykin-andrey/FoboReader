package ru.mamykin.foboreader.ui.devicebooks

import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.data.model.AndroidFile
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksPresenter
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity
import ru.mamykin.foboreader.ui.global.FileViewHolder
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksView
import ru.mamykin.foboreader.ui.devicebooks.list.FilesRecyclerAdapter

/**
 * Страница с файлами на устройстве
 */
class DeviceBooksFragment : MvpAppCompatFragment(), DeviceBooksView, SearchView.OnQueryTextListener {

    @BindView(R.id.rvFiles)
    protected var rvFiles: RecyclerView? = null
    @BindView(R.id.tvCurrentDir)
    protected var tvCurrentDir: TextView? = null
    @BindView(R.id.ibUp)
    protected var btnUpDir: View? = null

    @InjectPresenter
    internal var presenter: DeviceBooksPresenter? = null

    private var adapter: FilesRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @ProvidePresenter
    internal fun provideDeviceBooksPresenter(): DeviceBooksPresenter {
        return DeviceBooksPresenter(arguments.getString(CURRENT_DIR_EXTRA))
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_device_books, container, false)
        ButterKnife.bind(this, contentView)

        adapter = FilesRecyclerAdapter(object : FileViewHolder.OnItemClickListener {
            override fun onItemClick(position: Int) {
                presenter!!.onFileClicked(adapter!!.getItem(position))
            }
        })
        UiUtils.setupRecyclerView(context, rvFiles!!, adapter!!, LinearLayoutManager(context), true)

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
        adapter!!.changeData(files)
    }

    override fun setCurrentDir(currentDir: String) {
        tvCurrentDir!!.text = currentDir
    }

    override fun showPermissionMessage() {
        UiUtils.showToast(context, R.string.permission_denied)
    }

    override fun openBook(path: String) {
        startActivity(ReadBookActivity.getStartIntent(context, path))
    }

    override fun showUpDir(show: Boolean) {
        UiUtils.setVisibility(btnUpDir!!, show)
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
        private val CURRENT_DIR_EXTRA = "current_dir_extra"

        fun newInstance(currentDir: String?): DeviceBooksFragment {
            val args = Bundle()
            args.putString(CURRENT_DIR_EXTRA, currentDir
                    ?: Environment.getExternalStorageDirectory().toString())
            val fragment = DeviceBooksFragment()
            fragment.arguments = args
            return fragment
        }
    }
}