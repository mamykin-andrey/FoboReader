package ru.mamykin.foboreader.presentation.booksstore

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.mamykin.foboreader.entity.booksstore.FeaturedCategory
import ru.mamykin.foboreader.entity.booksstore.PromotedCategory
import ru.mamykin.foboreader.entity.booksstore.StoreCategory

@StateStrategyType(AddToEndSingleStrategy::class)
interface BooksStoreView : MvpView {

    fun showPromotedCategories(categories: List<PromotedCategory>)

    fun showFeaturedCategories(featured: List<FeaturedCategory>)

    fun showStoreCategories(categories: List<StoreCategory>)

    fun showMessage(message: String)

    fun showLoading(show: Boolean)
}