package ru.mamykin.foboreader.ui.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.mamykin.foboreader.entity.StoreBook
import ru.mamykin.foboreader.entity.booksstore.FeaturedCategory

class BooksStoreRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_PROMOTED_BOOKS = 0
        const val VIEW_TYPE_FEATURED_CATEGORIES = 1
    }

    private val viewHolderFactory = BooksStoreViewHolderFactory()
    private var books: List<StoreBook> = listOf()
    private var featuredCategories: List<FeaturedCategory> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            viewHolderFactory.create(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (getItemViewType(position)) {
        VIEW_TYPE_FEATURED_CATEGORIES -> (holder as FeaturedCategoriesViewHolder).bind(featuredCategories)
        else -> (holder as PromotedBookViewHolder).bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    override fun getItemViewType(position: Int): Int = when (position) {
        0 -> VIEW_TYPE_FEATURED_CATEGORIES
        else -> VIEW_TYPE_PROMOTED_BOOKS
    }

    fun changePromotedBooks(books: List<StoreBook>) {
        this.books = books
        notifyDataSetChanged()
    }

    fun changeFeaturedCategories(categories: List<FeaturedCategory>) {
        this.featuredCategories = categories
        notifyDataSetChanged()
    }
}