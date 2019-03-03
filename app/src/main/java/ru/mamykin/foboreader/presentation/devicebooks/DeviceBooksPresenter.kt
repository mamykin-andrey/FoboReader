package ru.mamykin.foboreader.presentation.devicebooks

import android.Manifest
import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.PermissionsManager
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.domain.devicebooks.DeviceBooksInteractor
import ru.mamykin.foboreader.domain.devicebooks.FileStructureEntity
import java.io.File
import javax.inject.Inject

@InjectViewState
class DeviceBooksPresenter @Inject constructor(
        private val interactor: DeviceBooksInteractor,
        private val permissionsManager: PermissionsManager,
        override val resourcesManager: ResourcesManager,
        override val schedulers: Schedulers
) : BasePresenter<DeviceBooksView>() {

    var router: DeviceBooksRouter? = null

    override fun attachView(view: DeviceBooksView) {
        super.attachView(view)
        checkHasStoragePermission()
    }

    fun checkHasStoragePermission() {
        if (permissionsManager.hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            openRootDirectory()
        } else {
            viewState.showNoPermissionView()
        }
    }

    fun onFileClicked(file: File) {
        interactor.openFile(file)
                .subscribe({ router?.openBook(it) }, { onError(R.string.error_unexpected_file_format) })
                .unsubscribeOnDestroy()
    }

    fun onDirectoryClicked(directoryPath: String) {
        interactor.getDirectoryFiles(directoryPath)
                .subscribe({ showFiles(it) }) { onError(R.string.error_access_denied) }
                .unsubscribeOnDestroy()
    }

    fun onParentDirectoryClicked() {
        interactor.getParentDirectoryFiles()
                .subscribe({ showFiles(it) }) { onError(R.string.error_access_denied) }
                .unsubscribeOnDestroy()
    }

    private fun openRootDirectory() {
        interactor.getRootDirectoryFiles()
                .subscribe({ showFiles(it) }, { onError(R.string.error_access_denied) })
                .unsubscribeOnDestroy()
    }

    private fun showFiles(structure: FileStructureEntity) = with(viewState) {
        showParentDirAvailable(structure.isParentDirAvailable)
        showCurrentDir(structure.directoryName)
        showFiles(structure.files)
    }
}