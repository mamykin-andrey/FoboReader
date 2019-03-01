package ru.mamykin.foboreader.presentation.mybooks

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.mamykin.foboreader.core.ui.BaseView
import ru.mamykin.foboreader.domain.entity.FictionBook

@StateStrategyType(AddToEndSingleStrategy::class)
interface MyBooksView : BaseView {

    fun showBooks(books: List<FictionBook>)
}