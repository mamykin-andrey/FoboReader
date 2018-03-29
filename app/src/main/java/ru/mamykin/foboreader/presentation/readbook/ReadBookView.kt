package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ReadBookView : MvpView {

    fun showLoading(show: Boolean)

    fun showParagraphLoading(show: Boolean)

    fun showWordLoading(show: Boolean)

    fun displayBookName(name: String)

    fun displayCurrentPage(page: Int)

    fun displayReadPages(text: Int)

    fun displayReadPercent(percent: Float)

    fun displaySourceParagraph()

    fun displayParagraphTranslation(text: String)

    fun displayWordTranslation(originalAndTranslation: Pair<String, String>)

    fun initBookView(title: String, text: String)

    fun showPageText(text: String)

    fun showBookContent(show: Boolean)
}