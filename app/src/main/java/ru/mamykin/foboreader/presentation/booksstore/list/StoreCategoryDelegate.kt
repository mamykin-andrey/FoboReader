package ru.mamykin.foboreader.presentation.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_store_category.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.foboreader.domain.entity.booksstore.StoreCategory

class StoreCategoryDelegate : AdapterDelegate<StoreCategoryViewHolder, StoreCategory>() {

    override fun isForViewType(item: Any): Boolean = item is StoreCategory

    override fun getLayoutId(): Int = R.layout.item_store_category

    override fun createViewHolder(itemView: View) = StoreCategoryViewHolder(itemView)

    override fun innerBindViewHolder(holder: StoreCategoryViewHolder, item: StoreCategory) = holder.bind(item)
}

class StoreCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(category: StoreCategory) = with(itemView) {
        tvCategoryName.text = category.title
    }
}