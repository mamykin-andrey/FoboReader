package ru.mamykin.foboreader.presentation.devicebooks

import android.Manifest
import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.devicebooks.AccessDeniedException
import ru.mamykin.foboreader.domain.devicebooks.DeviceBooksInteractor
import ru.mamykin.foboreader.domain.devicebooks.FileStructureEntity
import ru.mamykin.foboreader.domain.devicebooks.UnknownBookFormatException
import ru.mamykin.foboreader.extension.applySchedulers
import ru.mamykin.foboreader.presentation.global.BasePresenter
import ru.mamykin.foboreader.presentation.global.PermissionsManager
import ru.mamykin.foboreader.ui.devicebooks.DeviceBooksRouter
import java.io.File
import javax.inject.Inject

// TODO: remove dependency to Android SDK (permission)
@InjectViewState
class DeviceBooksPresenter @Inject constructor(
        private val interactor: DeviceBooksInteractor,
        private val router: DeviceBooksRouter,
        private val permissionsManager: PermissionsManager
) : BasePresenter<DeviceBooksView>() {

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
                .subscribe(router::openBook, this::onError)
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
                .applySchedulers()
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
        showCurrentDir(structure.currentDIr)
        showFiles(structure.files)
    }
}