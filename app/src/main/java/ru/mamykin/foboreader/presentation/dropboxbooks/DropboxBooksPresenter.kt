package ru.mamykin.foboreader.presentation.dropboxbooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.applySchedulers
import ru.mamykin.foboreader.domain.dropboxbooks.DropboxBooksInteractor
import ru.mamykin.foboreader.domain.entity.DropboxFile
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.core.platform.ResourcesManager
import javax.inject.Inject

@InjectViewState
class DropboxBooksPresenter @Inject constructor(
        private val interactor: DropboxBooksInteractor,
        private val router: DropboxBooksRouter,
        private val resourcesManager: ResourcesManager
) : BasePresenter<DropboxView>() {

    override fun attachView(view: DropboxView?) {
        super.attachView(view)
        if (interactor.isAuthorized()) {
            openRootDirectory()
        } else {
            viewState.showAuth()
        }
    }

    fun onLoginClicked() {
        router.startOAuth2Authentication()
    }

    fun onFileClicked(position: Int, file: DropboxFile) {
        interactor.downloadFile(file)
                .applySchedulers()
                .doOnSubscribe { viewState.showLoadingItem(position) }
                .doAfterTerminate { viewState.hideLoadingItem() }
                .subscribe(
                        { router.openBook(it) },
                        { viewState.onError(resourcesManager.getString(R.string.dropbox_download_file_error)) }
                )
                .unsubscribeOnDestroy()
    }

    fun onDirectoryClicked(dir: DropboxFile) {
        interactor.getDirectoryFiles(dir)
                .applySchedulers()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe(viewState::showFiles, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onParentDirectoryClicked() {
        interactor.getParentDirectoryFiles()
                .applySchedulers()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe(viewState::showFiles, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    private fun openRootDirectory() {
        interactor.getRootDirectoryFiles()
                .applySchedulers()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe({ viewState.showFiles(it) }, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }
}