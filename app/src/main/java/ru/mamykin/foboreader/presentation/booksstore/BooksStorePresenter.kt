package ru.mamykin.foboreader.presentation.booksstore

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.booksstore.BooksStoreInteractor
import ru.mamykin.foboreader.entity.booksstore.BooksStoreResponse
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
                .applySchedulers()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe(this::showStoreInfo, this::displayLoadingError)
                .unsubscribeOnDestroy()
    }

    private fun showStoreInfo(response: BooksStoreResponse) = with(viewState) {
        showPromotedCategories(response.promotions)
        showFeaturedCategories(response.featured)
        showStoreCategories(response.categories)
    }

    private fun displayLoadingError(e: Throwable) = with(viewState) {
        showMessage(e.localizedMessage)
    }
}