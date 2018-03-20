package ru.mamykin.foreignbooksreader.presentation.store

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.mamykin.foreignbooksreader.models.StoreBook

@StateStrategyType(AddToEndSingleStrategy::class)
interface BooksStoreView : MvpView {
    fun showBooks(booksList: List<StoreBook>)

    fun showMessage(message: String)

    fun showLoading(show: Boolean)
}