package ru.mamykin.foboreader.presentation.mybooks.list

import android.graphics.BitmapFactory
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_book.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.foboreader.domain.entity.FictionBook

class MyBookDelegate(
        private val onBookClicked: (String) -> Unit,
        private val onBookAboutClicked: (String) -> Unit,
        private val onBookShareClicked: (String) -> Unit,
        private val onBookRemoveClicked: (String) -> Unit
) : AdapterDelegate<FictionBook>() {

    override fun isForViewType(item: FictionBook): Boolean = true

    override fun getLayoutId(): Int = R.layout.item_book

    override fun createViewHolder(itemView: View): RecyclerView.ViewHolder = BookViewHolder(
            itemView,
            onBookClicked,
            onBookAboutClicked,
            onBookShareClicked,
            onBookRemoveClicked
    )

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: FictionBook) {
        (holder as BookViewHolder).bind(item)
    }
}

class BookViewHolder(itemView: View,
                     private val onBookClicked: (String) -> Unit,
                     private val onBookAboutClicked: (String) -> Unit,
                     private val onBookShareClicked: (String) -> Unit,
                     private val onBookRemoveClicked: (String) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind(book: FictionBook) = with(itemView) {
        setOnClickListener { onBookClicked(book.filePath) }
        btnMenu.setOnClickListener {
            PopupMenu(context, itemView).apply {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_about_book -> onBookAboutClicked(book.filePath)
                        R.id.menu_share_book -> onBookShareClicked(book.filePath)
                        R.id.menu_remove_book -> onBookRemoveClicked(book.filePath)
                    }
                    return@setOnMenuItemClickListener true
                }
                inflate(R.menu.menu_book_item)
                show()
            }
        }
        ivBookCover!!.setImageBitmap(BitmapFactory.decodeResource(context!!.resources, R.drawable.img_no_image))
        tvBookTitle.text = book.bookTitle
        tvAuthor.text = book.bookAuthor
        pvProgress.setPercentage(book.readPercent)
        tvBooksPages.text = book.pagesCountString
        tvBookAddedDate.text = book.lastOpenString
        tvFormat.isVisible = book.isFbWtBook
    }
}