package ru.mamykin.foboreader.presentation.booksstore

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.applySchedulers
import ru.mamykin.foboreader.core.platform.ResourcesManager
import ru.mamykin.foboreader.core.ui.BasePresenter
import ru.mamykin.foboreader.domain.booksstore.BooksStoreInteractor
import ru.mamykin.foboreader.domain.entity.booksstore.BooksStoreResponse
import javax.inject.Inject

@InjectViewState
class BooksStorePresenter @Inject constructor(
        private val interactor: BooksStoreInteractor,
        private val resourcesManager: ResourcesManager
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
                .subscribe(
                        { showStoreInfo(it) },
                        { viewState.onError(resourcesManager.getString(R.string.books_store_load_error)) }
                )
                .unsubscribeOnDestroy()
    }

    private fun showStoreInfo(response: BooksStoreResponse) = with(response) {
        viewState.showStoreBooks(promotions + featured + categories)
    }
}