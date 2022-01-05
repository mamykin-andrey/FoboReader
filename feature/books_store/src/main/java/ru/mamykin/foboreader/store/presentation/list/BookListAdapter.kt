package ru.mamykin.foboreader.store.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mamykin.foboreader.core.extension.getLayoutInflater
import ru.mamykin.foboreader.core.presentation.list.SimpleDiffUtil
import ru.mamykin.foboreader.store.databinding.ItemStoreBookBinding
import ru.mamykin.foboreader.store.domain.model.StoreBook

internal class BookListAdapter(
    private val onClick: (bookLink: String, fileName: String) -> Unit,
) : ListAdapter<StoreBook, BookListAdapter.BookViewHolder>(
    SimpleDiffUtil.equalsCallback { it.id }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BookViewHolder(
        ItemStoreBookBinding.inflate(parent.getLayoutInflater(), parent, false)
    ) { onClick(getItem(it).link, getItem(it).getFileName()) }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    internal class BookViewHolder(
        private val binding: ItemStoreBookBinding,
        private val onClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick(absoluteAdapterPosition)
            }
        }

        fun bind(book: StoreBook) = binding.apply {
            Picasso.with(itemView.context).load(book.cover).into(ivBookCover)
            tvBookName.text = book.title
            tvBookAuthor.text = book.author
            tvBookGenre.text = book.genre
        }
    }
}