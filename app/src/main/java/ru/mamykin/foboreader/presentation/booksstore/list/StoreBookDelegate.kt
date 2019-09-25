package ru.mamykin.foboreader.presentation.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_store_book.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.foboreader.domain.entity.StoreBook

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