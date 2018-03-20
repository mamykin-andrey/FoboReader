package ru.mamykin.foreignbooksreader.presentation.mybooks

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.mamykin.foreignbooksreader.data.model.FictionBook

@StateStrategyType(AddToEndSingleStrategy::class)
interface MyBooksView : MvpView {
    fun showEmptyStateView()

    fun showBooksList(booksList: List<FictionBook>)

    fun openBook(bookId: Int)

    fun openBookDetails(bookId: Int)

    fun showBookShareDialog(bookName: String, url: String)

    fun showBookShareDialog(bookName: String)
}