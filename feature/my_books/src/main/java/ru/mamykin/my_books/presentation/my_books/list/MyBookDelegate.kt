package ru.mamykin.my_books.presentation.my_books.list

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_book.view.*
import ru.mamykin.core.extension.showPopupMenu
import ru.mamykin.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.my_books.R
import ru.mamykin.my_books.domain.model.BookInfo

class MyBookDelegate(
        private val onAction: (BookAction, Long) -> Unit
) : AdapterDelegate<BookInfo>() {

    override fun isForViewType(item: BookInfo): Boolean = true

    override fun getLayoutId(): Int = R.layout.item_book

    override fun createViewHolder(itemView: View) = BookViewHolder(itemView, onAction)

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: BookInfo) {
        (holder as BookViewHolder).bind(item)
    }
}

class BookViewHolder(
        itemView: View,
        private val onAction: (BookAction, Long) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind(book: BookInfo) = with(itemView) {
        setOnClickListener { onAction(BookAction.Open, book.id) }
        btnMenu.setOnClickListener {
            btnMenu.showPopupMenu(
                    R.menu.menu_book_item,
                    R.id.menu_about_book to { onAction(BookAction.About, book.id) },
                    R.id.menu_remove_book to { onAction(BookAction.Remove, book.id) }
            )
        }
        //ivBookCover!!.setImageBitmap(BitmapFactory.decodeResource(context!!.resources, R.drawable.img_no_image))
        tvBookTitle.text = book.title
        tvAuthor.text = book.author
        // pvProgress.setPercentage(book.readPercent)
        // tvBooksPages.text = book.pagesCountString
        tvFileInfo.text = "${book.getFormat()}, ${book.getSize()}"
    }
}

sealed class BookAction {
    object Open : BookAction()
    object About : BookAction()
    object Remove : BookAction()
}