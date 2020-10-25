package ru.mamykin.foboreader.my_books.presentation.list

import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.loadImage
import ru.mamykin.foboreader.core.extension.showPopupMenu
import ru.mamykin.foboreader.my_books.R
import ru.mamykin.foboreader.my_books.databinding.ItemBookBinding

class BookViewHolder(
    private val binding: ItemBookBinding,
    private val onAboutClicked: (Long) -> Unit,
    private val onRemoveClicked: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(book: BookInfo) = binding.apply {
        bindBookMenu(book)
        bindBookCover(book)
        tvBookTitle.text = book.title
        tvAuthor.text = book.author
        pvProgress.progress = book.getReadPercent()
        bindBookPagesInfo(book)
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
        ivBookCover.loadImage(book.coverUrl, R.drawable.img_no_image)
    }

    private fun bindBookPagesInfo(book: BookInfo) = binding.apply {
        tvBooksPages.text = itemView.context.getString(
            R.string.book_pages_info,
            book.currentPage,
            book.totalPages
        )
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