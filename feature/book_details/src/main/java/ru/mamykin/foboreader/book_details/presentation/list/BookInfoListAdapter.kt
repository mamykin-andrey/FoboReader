package ru.mamykin.foboreader.book_details.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.book_details.databinding.ItemBookInfoBinding
import ru.mamykin.foboreader.book_details.presentation.model.BookInfoViewItem
import ru.mamykin.foboreader.core.extension.getLayoutInflater
import ru.mamykin.foboreader.core.presentation.list.SimpleDiffUtil

internal class BookInfoListAdapter : ListAdapter<BookInfoViewItem, BookInfoListAdapter.BookInfoViewHolder>(
    SimpleDiffUtil.equalsCallback { it.title }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BookInfoViewHolder(
        ItemBookInfoBinding.inflate(parent.getLayoutInflater(), parent, false)
    )

    override fun onBindViewHolder(holder: BookInfoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    internal class BookInfoViewHolder(
        private val binding: ItemBookInfoBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BookInfoViewItem) = binding.apply {
            tvBookInfoTitle.text = item.title
            tvBookInfoValue.text = item.value
        }
    }
}