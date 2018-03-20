package ru.mamykin.foreignbooksreader.presentation.store

import com.arellomobile.mvp.InjectViewState
import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.extension.applySchedulers
import ru.mamykin.foreignbooksreader.data.model.StoreBook
import ru.mamykin.foreignbooksreader.presentation.global.BasePresenter
import ru.mamykin.foreignbooksreader.data.network.BooksStoreService
import rx.Subscriber
import javax.inject.Inject

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@InjectViewState
class StorePresenter : BasePresenter<BooksStoreView>() {
    private var booksList: List<StoreBook>? = null
    @Inject
    lateinit var booksService: BooksStoreService

    init {
        ReaderApp.component.inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        loadStoreCategories()
    }

    fun loadStoreCategories() {
        viewState.showLoading(true)
        val subscription = booksService!!.books
                .applySchedulers()
                .subscribe(object : Subscriber<List<StoreBook>>() {
                    override fun onCompleted() {
                        viewState.showLoading(false)
                        viewState.showBooks(booksList!!)
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