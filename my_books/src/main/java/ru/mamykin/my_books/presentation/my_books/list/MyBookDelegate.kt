package ru.mamykin.my_books.presentation.my_books.list

import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_book.view.*
import ru.mamykin.core.ui.adapterdelegates.AdapterDelegate
import ru.mamykin.my_books.R
import ru.mamykin.my_books.domain.model.BookInfo
import ru.mamykin.my_books.presentation.my_books.MyBooksViewModel

class MyBookDelegate(
        private val onAction: (MyBooksViewModel.Event) -> Unit
) : AdapterDelegate<BookInfo>() {

    override fun isForViewType(item: BookInfo): Boolean = true

    override fun getLayoutId(): Int = R.layout.item_book

    override fun createViewHolder(itemView: View): RecyclerView.ViewHolder = BookViewHolder(
            itemView,
            onAction
    )

    override fun bindViewHolder(holder: RecyclerView.ViewHolder, item: BookInfo) {
        (holder as BookViewHolder).bind(item)
    }
}

class BookViewHolder(
        itemView: View,
        private val onAction: (MyBooksViewModel.Event) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    fun bind(book: BookInfo) = with(itemView) {
        setOnClickListener { onAction(MyBooksViewModel.Event.OnBookClicked(book.id)) }
        btnMenu.setOnClickListener {
            PopupMenu(context, itemView).apply {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.menu_about_book -> onAction(MyBooksViewModel.Event.OnBookAboutClicked(book.id))
                        R.id.menu_remove_book -> onAction(MyBooksViewModel.Event.OnBookRemoveClicked(book.id))
                    }
                    return@setOnMenuItemClickListener true
                }
                inflate(R.menu.menu_book_item)
                show()
            }
        }
        //ivBookCover!!.setImageBitmap(BitmapFactory.decodeResource(context!!.resources, R.drawable.img_no_image))
        tvBookTitle.text = book.title
        tvAuthor.text = book.author
        // pvProgress.setPercentage(book.readPercent)
        // tvBooksPages.text = book.pagesCountString
        // tvBookAddedDate.text = book.lastOpenString
        // tvFormat.isVisible = book.isFbWtBook
    }
}