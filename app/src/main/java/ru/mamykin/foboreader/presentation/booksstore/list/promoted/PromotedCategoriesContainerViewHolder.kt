package ru.mamykin.foboreader.presentation.booksstore.list.promoted

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_promoted_categories_container.view.*
import ru.mamykin.foboreader.domain.entity.booksstore.PromotedCategory

class PromotedCategoriesContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(categories: List<PromotedCategory>) = with(itemView) {
        rvFeaturedCategories.adapter = PromotedCategoriesRecyclerAdapter(categories)
    }
}