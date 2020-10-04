package ru.mamykin.foboreader.book_details.presentation.list

import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.book_details.databinding.ItemBookInfoBinding
import ru.mamykin.foboreader.book_details.presentation.model.BookInfoItem

class BookInfoViewHolder(
    private val binding: ItemBookInfoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: BookInfoItem) = binding.apply {
        tvBookInfoTitle.text = item.title
        tvBookInfoValue.text = item.value
    }
}