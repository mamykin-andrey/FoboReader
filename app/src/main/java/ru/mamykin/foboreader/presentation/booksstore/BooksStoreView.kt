package ru.mamykin.foboreader.presentation.booksstore

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.mamykin.foboreader.core.ui.BaseView

@StateStrategyType(AddToEndSingleStrategy::class)
interface BooksStoreView : BaseView {

    fun showStoreBooks(books: List<Any>)

    fun showMessage(message: String)

    fun showLoading(show: Boolean)
}