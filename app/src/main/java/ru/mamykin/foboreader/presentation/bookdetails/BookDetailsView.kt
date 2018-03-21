package ru.mamykin.foboreader.presentation.bookdetails

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface BookDetailsView : MvpView {
    fun openBook(bookId: Int)

    fun showTitle(title: String)

    fun setHomeEnabled(enabled: Boolean)

    fun showBookName(name: String)

    fun showBookAuthor(author: String)

    fun showBookPath(path: String)

    fun showBookCurrentPage(currentPage: String)

    fun showBookGenre(genre: String)

    fun showBookOriginalLang(lang: String)

    fun showBookCreatedDate(date: String)
}