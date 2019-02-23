package ru.mamykin.foboreader.presentation.bookdetails

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.mamykin.foboreader.core.ui.BaseView
import java.util.*

@StateStrategyType(AddToEndSingleStrategy::class)
interface BookDetailsView : BaseView {

    fun showTitle(title: String)

    fun setHomeEnabled(enabled: Boolean)

    fun showBookName(name: String)

    fun showBookAuthor(author: String)

    fun showBookPath(path: String)

    fun showBookCurrentPage(currentPage: String)

    fun showBookGenre(genre: String)

    fun showBookOriginalLang(lang: String)

    fun showBookCreatedDate(date: Date)
}