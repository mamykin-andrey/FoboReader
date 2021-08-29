package ru.mamykin.foboreader.my_books.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.presentation.list.SimpleDiffUtil
import ru.mamykin.foboreader.my_books.databinding.ItemBookBinding

class BookAdapter(
    private val onBookClicked: (Long) -> Unit,
    private val onAboutClicked: (Long) -> Unit,
    private val onRemoveClicked: (Long) -> Unit
) : ListAdapter<BookInfo, BookViewHolder>(SimpleDiffUtil.equalsCallback(BookInfo::id)) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        return BookViewHolder(
            ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            { onBookClicked(getItem(it).id) },
            { onAboutClicked(getItem(it).id) },
            { onRemoveClicked(getItem(it).id) }
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}