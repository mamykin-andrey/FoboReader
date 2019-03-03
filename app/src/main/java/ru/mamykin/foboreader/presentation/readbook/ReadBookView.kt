package ru.mamykin.foboreader.presentation.readbook

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.mamykin.foboreader.core.ui.BaseView

@StateStrategyType(AddToEndSingleStrategy::class)
interface ReadBookView : BaseView {

    fun showParagraphLoading(show: Boolean)

    fun showParagraphTranslation(text: String)

    fun showWordLoading(show: Boolean)

    fun showWordTranslation(word: String, translation: String)

    fun showBookName(name: String)
}