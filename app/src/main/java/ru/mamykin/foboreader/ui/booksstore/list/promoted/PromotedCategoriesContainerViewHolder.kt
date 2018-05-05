package ru.mamykin.foboreader.ui.booksstore.list.promoted

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_promoted_categories_container.view.*
import ru.mamykin.foboreader.entity.booksstore.PromotedCategory
import ru.mamykin.foboreader.ui.booksstore.list.promoted.PromotedCategoriesRecyclerAdapter

class PromotedCategoriesContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(categories: List<PromotedCategory>) {
        val adapter = PromotedCategoriesRecyclerAdapter(categories)
        itemView.rvFeaturedCategories.adapter = adapter
    }
}