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
    private val onBookClicked: (Int) -> Unit,
    private val onAboutClicked: (Int) -> Unit,
    private val onRemoveClicked: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            onBookClicked(adapterPosition)
        }
    }

    fun bind(book: BookInfo) = binding.apply {
        bindBookMenu()
        bindBookCover(book)
        tvBookTitle.text = book.title
        tvAuthor.text = book.author
        pvProgress.progress = book.getReadPercent()
        bindBookPagesInfo(book)
        bindFileInfo(book)
    }

    private fun bindBookMenu() = binding.btnMenu.apply {
        setOnClickListener {
            showPopupMenu(
                R.menu.menu_book_item,
                R.id.menu_about_book to { onAboutClicked(adapterPosition) },
                R.id.menu_remove_book to { onRemoveClicked(adapterPosition) }
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