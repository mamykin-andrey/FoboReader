package ru.mamykin.foboreader.presentation.store

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.domain.booksstore.BooksStoreInteractor
import ru.mamykin.foboreader.presentation.global.BasePresenter
import javax.inject.Inject

@InjectViewState
class BooksStorePresenter @Inject constructor(
        private val interactor: BooksStoreInteractor
) : BasePresenter<BooksStoreView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadStoreCategories()
    }

    fun loadStoreCategories() {
        interactor.getStoreCategories()
                .doOnSubscribe { viewState.showLoading(true) }
                .doAfterTerminate { viewState.showLoading(false) }
                .subscribe(viewState::showBooks, this::displayLoadingError)
                .unsubscribeOnDestory()
    }

    private fun displayLoadingError(e: Throwable) {
        viewState.showMessage(e.localizedMessage)
    }
}