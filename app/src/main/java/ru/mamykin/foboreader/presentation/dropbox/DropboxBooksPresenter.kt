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
                .doAfterTerminate { viewState.showLoadingItem(null) }
                .subscribe(router::openBook, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onDirectoryClicked(dir: DropboxFile) {
        interactor.getDirectoryFiles(dir)
                .applySchedulers()
                .showProgress()
                .subscribe(viewState::showFiles, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    fun onParentDirectoryClicked() {
        interactor.getParentDirectoryFiles()
                .applySchedulers()
                .showProgress()
                .subscribe(viewState::showFiles, Throwable::printStackTrace)
                .unsubscribeOnDestroy()
    }

    private fun openRootDirectory() {
        interactor.getRootDirectoryFiles()
                .applySchedulers()
                .showProgress()
                .subscribe({ viewState.showFiles(it) }, { viewState.showAuth(true) })
                .unsubscribeOnDestroy()
    }

    private fun <T> Single<T>.showProgress(): Single<T> {
        doOnSubscribe { viewState.showLoading(true) }
        doAfterTerminate { viewState.showLoading(false) }
        return this
    }
}