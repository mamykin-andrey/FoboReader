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
        loadRootDirectoryFiles()
    }

    fun onFileClicked(file: File) {
        interactor.openFile(file)
                .subscribe(router::openBook, this::showOpenFileError)
                .unsubscribeOnDestory()
    }

    fun onDirectoryClicked(dir: File) {
        interactor.openDirectory(dir.absolutePath)
                .subscribe({ showFiles(it) }, { viewState.showPermissionError() })
                .unsubscribeOnDestory()
    }

    fun onParentDirectoryClicked() {
        interactor.openParentDirectory()
                .subscribe({ showFiles(it) }, { viewState.showPermissionError() })
                .unsubscribeOnDestory()
    }

    private fun loadRootDirectoryFiles() {
        interactor.openRootDirectory()
                .subscribe({ showFiles(it) }, { viewState.showPermissionError() })
                .unsubscribeOnDestory()
    }

    private fun showOpenFileError(error: Throwable) {
        when (error) {
            is AccessDeniedException -> viewState.showPermissionError()
            is UnknownBookFormatException -> viewState.showBookFormatError()
        }
    }

    private fun showFiles(structure: FileStructureEntity) {
        viewState.showUpDir(structure.isUpDirAvailable)
        viewState.showCurrentDir(structure.currentDIr)
        viewState.showFiles(structure.files)
    }
}