package ru.mamykin.foboreader.presentation.mybooks

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

import ru.mamykin.foboreader.entity.FictionBook

@StateStrategyType(AddToEndSingleStrategy::class)
interface MyBooksView : MvpView {

    fun showBooks(books: List<FictionBook>)
}