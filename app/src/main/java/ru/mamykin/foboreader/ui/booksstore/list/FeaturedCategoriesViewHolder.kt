package ru.mamykin.foboreader.ui.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_featured_books.view.*
import ru.mamykin.foboreader.entity.booksstore.FeaturedCategory

class FeaturedCategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(categories: List<FeaturedCategory>) {
        val adapter = FeaturedCategoriesRecyclerAdapter(categories)
        itemView.rvFeaturedCategories.adapter = adapter
    }
}