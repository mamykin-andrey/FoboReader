package ru.mamykin.foboreader.presentation.booksstore.list.promoted

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.domain.entity.booksstore.PromotedCategory

class PromotedCategoriesRecyclerAdapter(
        private var categories: List<PromotedCategory>
) : RecyclerView.Adapter<PromotedCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotedCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_promoted_category, parent, false)
        return PromotedCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromotedCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}