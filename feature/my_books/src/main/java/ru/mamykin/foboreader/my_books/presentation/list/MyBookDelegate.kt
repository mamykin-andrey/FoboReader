package ru.mamykin.foboreader.my_books.presentation.list

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.showPopupMenu
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.databinding.ItemBookBinding

class MyBookDelegate(
    private val onBookClicked: (Long) -> Unit,
    private val onAboutClicked: (Long) -> Unit,
    private val onRemoveClicked: (Long) -> Unit
) : AdapterDelegate<BookInfo>() {

    override fun isForViewType(item: BookInfo): Boolean = true

    override fun getLayoutId(): Int = R.layout.item_book

    override fun createViewHolder(itemView: View) = BookViewHolder(
        ItemBookBinding.inflate(LayoutInflater.from(itemView.context)),
        onBookClicked,
        onAboutClicked,
        onRemoveClicked
    )

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: BookInfo) {
        (holder as BookViewHolder).bind(item)
    }
}

class BookViewHolder(
    private val binding: ItemBookBinding,
    private val onBookClicked: (Long) -> Unit,
    private val onAboutClicked: (Long) -> Unit,
    private val onRemoveClicked: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(book: BookInfo) = binding.apply {
        itemView.setOnClickListener { onBookClicked(book.id) }
        bindBookMenu(book)
        bindBookCover(book)
        tvBookTitle.text = book.title
        tvAuthor.text = book.author
//        pvProgress.setPercentage(book.readPercent)
        tvBooksPages.text = book.currentPage.toString()
        bindFileInfo(book)
    }

    private fun bindBookMenu(book: BookInfo) = binding.apply {
        btnMenu.setOnClickListener {
            btnMenu.showPopupMenu(
                R.menu.menu_book_item,
                R.id.menu_about_book to { onAboutClicked(book.id) },
                R.id.menu_remove_book to { onRemoveClicked(book.id) }
            )
        }
    }

    private fun bindBookCover(book: BookInfo) = binding.apply {
        ViewCompat.setTransitionName(ivBookCover, book.id.toString())
        book.coverUrl?.takeIf { it.isNotEmpty() }?.let {
            Picasso.with(itemView.context)
                .load(it)
                .error(R.drawable.img_no_image)
                .into(ivBookCover)
        }
    }

    private fun bindFileInfo(book: BookInfo) = binding.apply {
        tvFileInfo.text = itemView.context.getString(
            R.string.my_books_book_info_description_title,
            book.getFormat(),
            getFileSizeDescription(book.getFileSizeKb())
        )
    }

    private fun getFileSizeDescription(fileSizeKb: Long): String {
        return if (fileSizeKb > 1024) {
            "${fileSizeKb / 1024}MB"
        } else {
            "${fileSizeKb}KB"
        }
    }
}