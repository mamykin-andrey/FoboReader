package ru.mamykin.foboreader.presentation.devicebooks

import android.Manifest
import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.core.platform.PermissionsManager
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.domain.devicebooks.AccessDeniedException
import ru.mamykin.foboreader.domain.devicebooks.DeviceBooksInteractor
import ru.mamykin.foboreader.domain.devicebooks.FileStructureEntity
import ru.mamykin.foboreader.domain.devicebooks.UnknownBookFormatException
import java.io.File
import javax.inject.Inject

@InjectViewState
class DeviceBooksPresenter @Inject constructor(
        private val interactor: DeviceBooksInteractor,
        private val permissionsManager: PermissionsManager
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
                .subscribe({ router?.openBook(it) }, { onError(it) })
                .unsubscribeOnDestroy()
    }

    fun onDirectoryClicked(dir: File) {
        interactor.getDirectoryFiles(dir.absolutePath)
                .subscribe(this::showFiles, this::onError)
                .unsubscribeOnDestroy()
    }

    fun onParentDirectoryClicked() {
        interactor.getParentDirectoryFiles()
                .subscribe(this::showFiles, this::onError)
                .unsubscribeOnDestroy()
    }

    private fun openRootDirectory() {
        interactor.getRootDirectoryFiles()
                .subscribe(this::showFiles, this::onError)
                .unsubscribeOnDestroy()
    }

    private fun onError(error: Throwable) {
        when (error) {
            is AccessDeniedException -> viewState.showPermissionError()
            is UnknownBookFormatException -> viewState.showBookFormatError()
        }
    }

    private fun showFiles(structure: FileStructureEntity) = with(viewState) {
        showParentDirAvailable(structure.isParentDirAvailable)
        showCurrentDir(structure.directoryName)
        showFiles(structure.files)
    }
}