package ru.mamykin.foboreader.ui.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.mamykin.foboreader.entity.StoreBook
import ru.mamykin.foboreader.entity.booksstore.PromotedCategory
import ru.mamykin.foboreader.entity.booksstore.StoreCategory
import ru.mamykin.foboreader.ui.booksstore.list.categories.StoreCategoryViewHolder
import ru.mamykin.foboreader.ui.booksstore.list.featured.FeaturedBookViewHolder
import ru.mamykin.foboreader.ui.booksstore.list.promoted.PromotedCategoriesContainerViewHolder

class MainStoreRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_PROMOTED_CATEGORIES = 0
        const val VIEW_TYPE_FEATURED_BOOKS = 1
        const val VIEW_TYPE_STORE_CATEGORIES = 2
    }

    private var featuredBooks: List<StoreBook> = listOf()
    private var promotedCategories: List<PromotedCategory> = listOf()
    private var storeCategories: List<StoreCategory> = listOf()
    private val viewHolderFactory = MainStoreViewHolderFactory()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            viewHolderFactory.create(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_PROMOTED_CATEGORIES -> (holder as PromotedCategoriesContainerViewHolder).bind(promotedCategories)
            VIEW_TYPE_FEATURED_BOOKS -> (holder as FeaturedBookViewHolder).bind(featuredBooks[position])
            else -> (holder as StoreCategoryViewHolder).bind(storeCategories[position - featuredBooks.size])
        }
    }

    override fun getItemCount(): Int = featuredBooks.size + storeCategories.size

    override fun getItemViewType(position: Int): Int = when {
        position == 0 -> VIEW_TYPE_PROMOTED_CATEGORIES
        position < featuredBooks.size -> VIEW_TYPE_FEATURED_BOOKS
        else -> VIEW_TYPE_STORE_CATEGORIES
    }

    fun changeFeaturedBooks(books: List<StoreBook>) {
        this.featuredBooks = books
        notifyDataSetChanged()
    }

    fun changePromotedCategories(categories: List<PromotedCategory>) {
        this.promotedCategories = categories
        notifyDataSetChanged()
    }

    fun changeStoreCategories(categories: List<StoreCategory>) {
        this.storeCategories = categories
        notifyDataSetChanged()
    }
}