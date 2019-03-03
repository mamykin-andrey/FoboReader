package ru.mamykin.foboreader.presentation.dropboxbooks

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.platform.Schedulers
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.domain.dropboxbooks.DropboxBooksInteractor
import ru.mamykin.foboreader.domain.entity.DropboxFile
import javax.inject.Inject

@InjectViewState
class DropboxBooksPresenter @Inject constructor(
        private val interactor: DropboxBooksInteractor,
        override val resourcesManager: ResourcesManager,
        override val schedulers: Schedulers
) : BasePresenter<DropboxView>() {

    var router: DropboxBooksRouter? = null

    override fun attachView(view: DropboxView?) {
        super.attachView(view)
        if (interactor.isAuthorized()) {
            openRootDirectory()
        } else {
            viewState.showAuth()
        }
    }

    fun onLoginClicked() {
        router?.startOAuth2Authentication()
    }

    fun onFileClicked(file: DropboxFile) {
        interactor.downloadFile(file)
                .applySchedulers()
                //.doOnSubscribe { viewState.showLoadingItem(position) }
                //.doAfterTerminate { viewState.hideLoadingItem() }
                .subscribe({ router?.openBook(it) }, { onError(R.string.dropbox_download_file_error) })
                .unsubscribeOnDestroy()
    }

    fun onDirectoryClicked(dir: DropboxFile) {
        interactor.getDirectoryFiles(dir)
                .applySchedulers()
                .showProgress()
                .subscribe({ viewState.showFiles(it) }) { onError(R.string.error_directory_open) }
                .unsubscribeOnDestroy()
    }

//    fun onParentDirectoryClicked() {
//        interactor.getParentDirectoryFiles()
//                .applySchedulers()
//                .doOnSubscribe { viewState.showLoading(true) }
//                .doAfterTerminate { viewState.showLoading(false) }
//                .subscribe(viewState::showFiles, Throwable::printStackTrace)
//                .unsubscribeOnDestroy()
//    }

    private fun openRootDirectory() {
        interactor.getRootDirectoryFiles()
                .applySchedulers()
                .showProgress()
                .subscribe({ viewState.showFiles(it) }) { onError(R.string.error_directory_open) }
                .unsubscribeOnDestroy()
    }
}