package ru.mamykin.store.presentation.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_store_book.view.*
import ru.mamykin.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.store.R
import ru.mamykin.store.domain.model.StoreBook

class StoreBookDelegate : AdapterDelegate<Any>() {

    override fun isForViewType(item: Any): Boolean = item is StoreBook

    override fun getLayoutId(): Int = R.layout.item_store_book

    override fun createViewHolder(itemView: View) = StoreCategoryViewHolder(itemView)

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Any) {
        (holder as StoreCategoryViewHolder).bind(item as StoreBook)
    }
}

class StoreCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(book: StoreBook) = with(itemView) {
        tvBookName.text = book.title
        tvBookAuthor.text = book.author
        tvBookGenre.text = book.genre
    }
}