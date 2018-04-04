package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ReadBookView : MvpView {

    fun showLoading(show: Boolean)

    fun showParagraphLoading(show: Boolean)

    fun showWordLoading(show: Boolean)

    fun showBookName(name: String)

    fun showCurrentPage(page: Int)

    fun showReadPages(text: Int)

    fun showReadPercent(percent: Float)

    fun showSourceParagraph()

    fun showParagraphTranslation(text: String)

    fun showWordTranslation(textAndTranslation: Pair<String, String>)

    fun initBookView()

    fun showPageText(text: String)

    fun showBookContent(show: Boolean)
}