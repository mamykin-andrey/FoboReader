package ru.mamykin.foboreader.presentation.devicebooks

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_device_books.*
import kotlinx.android.synthetic.main.layout_no_permission.*
import ru.mamykin.foboreader.BuildConfig
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.presentation.devicebooks.list.FilesRecyclerAdapter
import java.io.File

/**
 * Страница с файлами на устройстве
 */
class DeviceBooksFragment : BaseFragment(), DeviceBooksView, SearchView.OnQueryTextListener {

    companion object {

        private const val STORAGE_PERMISSION_REQUEST_CODE = 1

        fun newInstance() = DeviceBooksFragment()
    }

    override val layoutId: Int = R.layout.fragment_device_books

    @InjectPresenter
    lateinit var presenter: DeviceBooksPresenter

    private lateinit var adapter: FilesRecyclerAdapter

    @ProvidePresenter
    fun provideDeviceBooksPresenter(): DeviceBooksPresenter = getAppComponent()
            .getDeviceBooksComponent()
            .getDeviceBooksPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFilesRecyclerView()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {
            presenter.checkHasStoragePermission()
        }
    }

    override fun showNoPermissionView() {
        clNoPermission.isVisible = true
        tvTitle.text = getString(R.string.no_storage_permission_title)
        tvSubtitle.text = getString(R.string.no_storage_permission_text)
        btnGrantPermission.setOnClickListener {
            val appPackage = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
            startActivityForResult(Intent(ACTION_APPLICATION_DETAILS_SETTINGS, appPackage), STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    override fun showFiles(files: List<File>) {
        clNoPermission.isVisible = false
        adapter.changeData(files)
    }

    override fun showCurrentDir(currentDir: String) {
        btnUp.text = currentDir
    }

    override fun showPermissionError() {
        showToast(R.string.permission_denied)
    }

    override fun showBookFormatError() {
        showToast(getString(R.string.wrong_file_format))
    }

    override fun showParentDirAvailable(show: Boolean) {
        btnUpDir.isVisible = show
    }

    override fun onQueryTextSubmit(query: String): Boolean = false

    override fun onQueryTextChange(newText: String): Boolean = false

    private fun initFilesRecyclerView() {
        adapter = FilesRecyclerAdapter(presenter::onFileClicked, presenter::onDirectoryClicked)
        rvFiles.adapter = adapter
        rvFiles.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}