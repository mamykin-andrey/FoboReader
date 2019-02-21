package ru.mamykin.foboreader.ui.booksstore.list.categories

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_store_category.view.*
import ru.mamykin.foboreader.entity.booksstore.StoreCategory

class StoreCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: StoreCategory) = with(itemView) {
        tvCategoryName.text = category.title
    }
}