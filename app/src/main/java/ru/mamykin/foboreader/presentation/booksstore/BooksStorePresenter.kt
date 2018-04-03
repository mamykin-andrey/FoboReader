package ru.mamykin.foboreader.presentation.booksstore

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.booksstore.BooksStoreInteractor
import ru.mamykin.foboreader.entity.BooksStoreResponse
import ru.mamykin.foboreader.extension.applySchedulers
import ru.mamykin.foboreader.presentation.global.BasePresenter
import javax.inject.Inject

@InjectViewState
class BooksStorePresenter @Inject constructor(
        private val interactor: BooksStoreInteractor
) : BasePresenter<BooksStoreView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadBooks()
    }

    fun loadBooks() {
        interactor.getBooks()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .applySchedulers()
                .subscribe(this::showStoreInfo, this::displayLoadingError)
                .unsubscribeOnDestory()
    }

    private fun showStoreInfo(response: BooksStoreResponse) {
        viewState.showBooks(response.featured[0].books)
    }

    private fun displayLoadingError(e: Throwable) {
        viewState.showMessage(e.localizedMessage)
    }
}