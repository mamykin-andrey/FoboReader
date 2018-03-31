package ru.mamykin.foboreader.presentation.dropbox

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.data.model.DropboxFile
import ru.mamykin.foboreader.domain.dropboxbooks.DropboxBooksInteractor
import ru.mamykin.foboreader.presentation.global.BasePresenter
import ru.mamykin.foboreader.ui.dropbox.DropboxBooksRouter
import javax.inject.Inject

@InjectViewState
class DropboxBooksPresenter @Inject constructor(
        private val interactor: DropboxBooksInteractor,
        private val router: DropboxBooksRouter
) : BasePresenter<DropboxView>() {

    override fun attachView(view: DropboxView) {
        super.attachView(view)
        initDropbox()
    }

    fun onLoginClicked() {
        interactor.login()
                .subscribe(router::startOAuth2Authentication)
                .unsubscribeOnDestory()
    }

    fun onFileClicked(position: Int, file: DropboxFile) {
        interactor.downloadFile(file)
                .doOnSubscribe { viewState.showLoadingItem(position) }
                .doAfterTerminate { viewState.hideLoadingItem() }
                .subscribe(this::openBook, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onDirectoryClicked(dir: DropboxFile) {
        interactor.openDirectory(dir)
                .subscribe(this::displayFiles, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    fun onParentDirectoryClicked() {
        interactor.openParentDirectory()
                .subscribe(this::displayFiles, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun initDropbox() {
        interactor.initDropbox()
                .subscribe({ showAuth() }, { loadRootFiles() })
                .unsubscribeOnDestory()
    }

    private fun loadRootFiles() {
        interactor.openRootDirectory()
                .subscribe(this::displayFiles, Throwable::printStackTrace)
                .unsubscribeOnDestory()
    }

    private fun displayFiles(files: List<DropboxFile>) {
        viewState.hideLoading()
        viewState.showFiles(files)
    }

    private fun showAuth() {
        viewState.hideFiles()
        viewState.showAuth()
    }

    private fun openBook(bookPath: String) {
        viewState.hideLoadingItem()
        router.openBook(bookPath)
    }
}