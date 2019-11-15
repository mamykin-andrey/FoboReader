package ru.mamykin.foboreader.presentation.devicebooks

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_device_books.*
import kotlinx.android.synthetic.main.layout_no_permission.*
import ru.mamykin.foboreader.BuildConfig
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.extension.showSnackbar
import ru.mamykin.foboreader.core.ui.BaseFragment
import ru.mamykin.foboreader.core.ui.UiUtils
import ru.mamykin.foboreader.data.model.FileStructure
import ru.mamykin.foboreader.presentation.devicebooks.list.FilesRecyclerAdapter

class DeviceBooksFragment : BaseFragment(R.layout.fragment_device_books) {

    companion object {

        private const val STORAGE_PERMISSION_REQUEST_CODE = 1

        fun newInstance() = DeviceBooksFragment()
    }

    private val viewModel: DeviceBooksViewModel by viewModel()
    private lateinit var adapter: FilesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFilesRecyclerView()
        btnUpDir.setOnClickListener {
            viewModel.onEvent(DeviceBooksViewModel.Event.OnParentDirectoryClicked)
        }
        initViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onEvent(DeviceBooksViewModel.Event.OnCheckPermissions)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_device_books, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        UiUtils.setupSearchView(
                context!!,
                menu!!,
                R.id.action_search,
                R.string.menu_search,
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return false
                    }
                }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE && resultCode == RESULT_OK) {
            viewModel.onEvent(DeviceBooksViewModel.Event.OnCheckPermissions)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.router = null
    }

    private fun initViewModel() {
        viewModel.router = DeviceBooksRouter(activity!!)
        viewModel.stateLiveData.observe(viewLifecycleOwner, Observer { state ->
            state.structure?.let(::showFileStructure)
            if (state.noPermission) showNoPermissionView()
            state.error?.let { showSnackbar(it) }
        })
    }

    private fun showFileStructure(structure: FileStructure) {
        clNoPermission.isVisible = false
        adapter.changeData(structure.files)
        btnUp.text = structure.directoryName
        btnUpDir.isVisible = structure.isParentDirAvailable
    }

    private fun showNoPermissionView() {
        clNoPermission.isVisible = true
        tvTitle.text = getString(R.string.no_storage_permission_title)
        tvSubtitle.text = getString(R.string.no_storage_permission_text)
        btnGrantPermission.setOnClickListener { openAppPermissions() }
    }

    private fun openAppPermissions() {
        val appPackage = Uri.parse("package:" + BuildConfig.APPLICATION_ID)
        startActivityForResult(
                Intent(ACTION_APPLICATION_DETAILS_SETTINGS, appPackage),
                STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    private fun initFilesRecyclerView() {
        adapter = FilesRecyclerAdapter(
                { viewModel.onEvent(DeviceBooksViewModel.Event.OnFileClicked(it)) },
                { viewModel.onEvent(DeviceBooksViewModel.Event.OnDirectoryClicked(it)) }
        )
        rvFiles.adapter = adapter
        rvFiles.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}