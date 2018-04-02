package ru.mamykin.foboreader.presentation.devicebooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.devicebooks.DeviceBooksInteractor
import ru.mamykin.foboreader.domain.devicebooks.FileStructureEntity
import ru.mamykin.foboreader.entity.AndroidFile
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
        interactor.openDirectory(dir.absolutePath)
                .subscribe({ showFiles(it) }, { viewState.showPermissionMessage() })
                .unsubscribeOnDestory()
    }

    fun onParentDirectoryClicked() {
        interactor.openParentDirectory()
                .subscribe({ showFiles(it) }, { viewState.showPermissionMessage() })
                .unsubscribeOnDestory()
    }

    private fun loadRootDirectoryFiles() {
        interactor.openRootDirectory()
                .subscribe({ showFiles(it) }, { viewState.showPermissionMessage() })
                .unsubscribeOnDestory()
    }

    private fun showFiles(structure: FileStructureEntity) {
        viewState.showUpDir(structure.isUpDirAvailable)
        viewState.showCurrentDir(structure.currentDIr)
        viewState.showFiles(structure.files)
    }
}