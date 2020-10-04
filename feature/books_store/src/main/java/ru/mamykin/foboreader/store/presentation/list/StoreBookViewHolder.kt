package ru.mamykin.foboreader.store.presentation.list

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mamykin.foboreader.store.databinding.ItemStoreBookBinding
import ru.mamykin.foboreader.store.domain.model.StoreBook

class StoreBookViewHolder(
    private val binding: ItemStoreBookBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(book: StoreBook) = binding.apply {
        Picasso.with(itemView.context).load(book.cover).into(ivBookCover)
        tvBookName.text = book.title
        tvBookAuthor.text = book.author
        tvBookGenre.text = book.genre
    }
}