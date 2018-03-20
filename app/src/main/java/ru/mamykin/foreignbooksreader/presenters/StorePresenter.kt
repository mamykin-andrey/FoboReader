package ru.mamykin.foreignbooksreader.presenters

import com.arellomobile.mvp.InjectViewState

import javax.inject.Inject

import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.common.Utils
import ru.mamykin.foreignbooksreader.models.StoreBook
import ru.mamykin.foreignbooksreader.retrofit.BooksStoreService
import ru.mamykin.foreignbooksreader.views.BooksStoreView
import rx.Subscriber
import rx.Subscription

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class StorePresenter : BasePresenter<BooksStoreView>() {
    private var booksList: List<StoreBook>? = null
    @Inject
    protected var booksService: BooksStoreService? = null

    init {
        ReaderApp.getComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadStoreCategories()
    }

    fun loadStoreCategories() {
        viewState.showLoading(true)
        val subscription = booksService!!.books
                .compose(Utils.applySchedulers())
                .subscribe(object : Subscriber<List<StoreBook>>() {
                    override fun onCompleted() {
                        viewState.showLoading(false)
                        viewState.showBooks(booksList)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        viewState.showLoading(false)
                        viewState.showMessage(e.localizedMessage)
                    }

                    override fun onNext(booksList: List<StoreBook>) {
                        this@StorePresenter.booksList = booksList
                    }
                })
        unsubscribeOnDestroy(subscription)
    }
}