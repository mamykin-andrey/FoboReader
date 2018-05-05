package ru.mamykin.foboreader.ui.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.booksstore.PromotedCategory

class FeaturedCategoriesRecyclerAdapter(
        private var categories: List<PromotedCategory>
) : RecyclerView.Adapter<FeaturedCategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeaturedCategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_featured_book, parent, false)
        return FeaturedCategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeaturedCategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size
}