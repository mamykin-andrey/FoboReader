package ru.mamykin.foboreader.store.presentation.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_store_book.view.*
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.domain.model.StoreBook

class StoreBookDelegate(
        private val onBookClicked: (StoreBook) -> Unit
) : AdapterDelegate<Any>() {

    override fun isForViewType(item: Any): Boolean = item is StoreBook

    override fun getLayoutId(): Int = R.layout.item_store_book

    override fun createViewHolder(itemView: View) =
            StoreCategoryViewHolder(itemView, onBookClicked)

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Any) {
        (holder as StoreCategoryViewHolder).bind(item as StoreBook)
    }
}

class StoreCategoryViewHolder(
        containerView: View,
        private val onBookClicked: (StoreBook) -> Unit
) : RecyclerView.ViewHolder(containerView) {

    fun bind(book: StoreBook) = with(itemView) {
        Picasso.with(context).load(book.cover).into(ivBookCover)
        tvBookName.text = book.title
        tvBookAuthor.text = book.author
        tvBookGenre.text = book.genre

        clBook.setOnClickListener { onBookClicked(book) }
    }
}