package ru.mamykin.foboreader.presentation.devicebooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.entity.AndroidFile
import ru.mamykin.foboreader.domain.devicebooks.DeviceBooksInteractor
import ru.mamykin.foboreader.domain.devicebooks.FileStructureEntity
import ru.mamykin.foboreader.presentation.global.BasePresenter
import ru.mamykin.foboreader.ui.devicebooks.DeviceBooksRouter
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

    fun onFileClicked(file: AndroidFile) {
        interactor.openFile(file)
                .subscribe({ router.openBook(it) }, { viewState.showPermissionMessage() })
                .unsubscribeOnDestory()
    }

    fun onDirectoryClicked(dir: AndroidFile) {
        interactor.openDirectory(dir)
                .subscribe({ loadFiles(it) }, { viewState.showPermissionMessage() })
                .unsubscribeOnDestory()
    }

    fun onUpDirClicked() {
        interactor.openParentDirectory()
                .subscribe(this::showFiles)
                .unsubscribeOnDestory()
    }

    private fun loadRootDirectoryFiles() {
        interactor.getRootDirectoryFiles()
                .subscribe(this::showFiles, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun loadFiles(currentDir: String) {
        interactor.getFiles(currentDir)
                .subscribe(this::showFiles)
                .unsubscribeOnDestory()
    }

    private fun showFiles(structure: FileStructureEntity) {
        viewState.showUpDir(structure.isUpDirAvailable)
        viewState.showCurrentDir(structure.currentDIr)
        viewState.showFiles(structure.files)
    }
}