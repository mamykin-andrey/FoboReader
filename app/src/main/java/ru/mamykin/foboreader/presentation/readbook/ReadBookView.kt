package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface ReadBookView : MvpView {
    fun showToast(text: String)

    fun showLoading(loading: Boolean)

    fun setBookName(name: String)

    fun setReadPages(text: String)

    fun setReadPercent(text: String)

    fun setSourceText(text: CharSequence)

    fun setTranslationText(text: String)

    fun showTranslationPopup(original: String, translation: String)

    fun initBookView(title: String, text: String)

    fun showBookContent(show: Boolean)
}