package ru.mamykin.foboreader.presentation.dropbox

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.dropboxbooks.DropboxBooksInteractor
import ru.mamykin.foboreader.entity.DropboxFile
import ru.mamykin.foboreader.extension.applySchedulers
import ru.mamykin.foboreader.presentation.global.BasePresenter
import ru.mamykin.foboreader.ui.dropbox.DropboxBooksRouter
import rx.Single
import javax.inject.Inject

@InjectViewState
class DropboxBooksPresenter @Inject constructor(
        private val interactor: DropboxBooksInteractor,
        private val router: DropboxBooksRouter
) : BasePresenter<DropboxView>() {

    override fun attachView(view: DropboxView?) {
        super.attachView(view)
        openRootDirectory()
    }

    fun onLoginClicked() = router.startOAuth2Authentication()

    fun onFileClicked(position: Int, file: DropboxFile) {
        interactor.downloadFile(file)
                .applySchedulers()
                .doOnSubscribe { viewState.showLoadingItem(position) }
                .doAfterTerminate { viewState.hideLoadingItem() }
                .subscribe(router::openBook, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onDirectoryClicked(dir: DropboxFile) {
        interactor.getDirectoryFiles(dir)
                .applySchedulers()
                .showProgress()
                .subscribe(this::showFiles, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onParentDirectoryClicked() {
        interactor.getParentDirectoryFiles()
                .applySchedulers()
                .showProgress()
                .subscribe(this::showFiles, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    private fun openRootDirectory() {
        interactor.getRootDirectoryFiles()
                .applySchedulers()
                .showProgress()
                .subscribe({ showFiles(it) }, { showAuth() })
                .unsubscribeOnDestroy()
    }

    private fun showFiles(files: List<DropboxFile>) {
        viewState.hideAuth()
        viewState.showFiles(files)
    }

    private fun showAuth() {
        viewState.hideFiles()
        viewState.showAuth()
    }

    private fun <T> Single<T>.showProgress(): Single<T> {
        doOnSubscribe(viewState::showLoading)
        doAfterTerminate(viewState::hideLoading)
        return this
    }
}