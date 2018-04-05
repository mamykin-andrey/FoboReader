package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ReadBookView : MvpView {

    fun initBookView()

    fun showLoading(show: Boolean)

    fun showParagraphLoading(show: Boolean)

    fun showParagraphTranslation(text: String)

    fun showWordLoading(show: Boolean)

    fun showWordTranslation(word: String, translation: String)

    fun showBookName(name: String)

    fun showReaded(currentPage: Int, pagesCount: Int)

    fun showReadPercent(percent: Float)

    fun showPageText(text: String)
}