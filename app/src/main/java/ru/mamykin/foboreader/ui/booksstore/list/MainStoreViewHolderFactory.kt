package ru.mamykin.foboreader.ui.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.ui.booksstore.list.MainStoreRecyclerAdapter.Companion.VIEW_TYPE_FEATURED_BOOKS
import ru.mamykin.foboreader.ui.booksstore.list.MainStoreRecyclerAdapter.Companion.VIEW_TYPE_PROMOTED_CATEGORIES
import ru.mamykin.foboreader.ui.booksstore.list.categories.StoreCategoryViewHolder
import ru.mamykin.foboreader.ui.booksstore.list.featured.FeaturedBookViewHolder
import ru.mamykin.foboreader.ui.booksstore.list.promoted.PromotedCategoriesContainerViewHolder

class MainStoreViewHolderFactory {

    fun create(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val resourceId = getResourceId(viewType)
        val itemView = LayoutInflater.from(parent.context).inflate(resourceId, parent, false)
        return when (viewType) {
            VIEW_TYPE_PROMOTED_CATEGORIES -> PromotedCategoriesContainerViewHolder(itemView)
            VIEW_TYPE_FEATURED_BOOKS -> FeaturedBookViewHolder(itemView)
            else -> StoreCategoryViewHolder(itemView)
        }
    }

    private fun getResourceId(viewType: Int): Int = when (viewType) {
        VIEW_TYPE_PROMOTED_CATEGORIES -> R.layout.item_promoted_categories_container
        VIEW_TYPE_FEATURED_BOOKS -> R.layout.item_featured_book
        else -> R.layout.item_store_category
    }
}