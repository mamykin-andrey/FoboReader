package ru.mamykin.foboreader.book_details.presentation.list

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.book_details.R
import ru.mamykin.foboreader.book_details.databinding.ItemBookInfoBinding
import ru.mamykin.foboreader.book_details.presentation.model.BookInfoItem
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate

class BookInfoDelegate : AdapterDelegate<BookInfoItem>() {

    override fun isForViewType(item: BookInfoItem): Boolean = true

    override fun getLayoutId(): Int = R.layout.item_book_info

    override fun createViewHolder(itemView: View) =
        BookInfoViewHolder(ItemBookInfoBinding.inflate(LayoutInflater.from(itemView.context)))

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: BookInfoItem) {
        (holder as BookInfoViewHolder).bind(item)
    }
}

class BookInfoViewHolder(
    private val binding: ItemBookInfoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BookInfoItem) = binding.apply {
        tvBookInfoTitle.text = item.title
        tvBookInfoValue.text = item.value
    }
}