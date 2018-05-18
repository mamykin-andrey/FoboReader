package ru.mamykin.foboreader.presentation.devicebooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.devicebooks.AccessDeniedException
import ru.mamykin.foboreader.domain.devicebooks.DeviceBooksInteractor
import ru.mamykin.foboreader.domain.devicebooks.FileStructureEntity
import ru.mamykin.foboreader.domain.devicebooks.UnknownBookFormatException
import ru.mamykin.foboreader.presentation.global.BasePresenter
import ru.mamykin.foboreader.ui.devicebooks.DeviceBooksRouter
import java.io.File
import javax.inject.Inject

@InjectViewState
class DeviceBooksPresenter @Inject constructor(
        private val interactor: DeviceBooksInteractor,
        private val router: DeviceBooksRouter
) : BasePresenter<DeviceBooksView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        openRootDirectory()
    }

    fun onFileClicked(file: File) {
        interactor.openFile(file)
                .subscribe(router::openBook, this::showOpenFileError)
                .unsubscribeOnDestroy()
    }

    fun onDirectoryClicked(dir: File) {
        interactor.getDirectoryFiles(dir.absolutePath)
                .subscribe({ showFiles(it) }, { viewState.showPermissionError() })
                .unsubscribeOnDestroy()
    }

    fun onParentDirectoryClicked() {
        interactor.getParentDirectoryFiles()
                .subscribe({ showFiles(it) }, { viewState.showPermissionError() })
                .unsubscribeOnDestroy()
    }

    private fun openRootDirectory() {
        interactor.getRootDirectoryFiles()
                .subscribe({ showFiles(it) }, { viewState.showPermissionError() })
                .unsubscribeOnDestroy()
    }

    private fun showOpenFileError(error: Throwable) {
        when (error) {
            is AccessDeniedException -> viewState.showPermissionError()
            is UnknownBookFormatException -> viewState.showBookFormatError()
        }
    }

    private fun showFiles(structure: FileStructureEntity) {
        viewState.showParentDirAvailable(structure.isParentDirAvailable)
        viewState.showCurrentDir(structure.currentDIr)
        viewState.showFiles(structure.files)
    }
}