package ru.mamykin.foboreader.ui.mybooks.list

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_book.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.extension.isVisible

class BookViewHolder(itemView: View,
                     private val listener: BooksRecyclerAdapter.OnBookClickListener
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val menuListener = PopupMenu.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.menu_about_book -> listener.onBookAboutClicked(adapterPosition)
            R.id.menu_share_book -> listener.onBookShareClicked(adapterPosition)
            R.id.menu_remove_book -> listener.onBookRemoveClicked(adapterPosition)
        }
        return@OnMenuItemClickListener true
    }

    init {
        itemView.setOnClickListener(this)
        itemView.btnMenu.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btnMenu) {
            onBookMenuClicked(view)
        } else {
            onBookClicked()
        }
    }

    fun bind(book: FictionBook) {
//        holder.ivBookCover!!.setImageBitmap(BitmapFactory
//                .decodeResource(context!!.resources, R.drawable.img_no_image))
        itemView.tvBookTitle!!.text = book.bookTitle
        itemView.tvAuthor.text = book.bookAuthor
        itemView.pvProgress.setPercents(book.percents)
        itemView.tvBooksPages.text = book.pagesCountString
        itemView.tvBookAddedDate!!.text = book.lastOpenString
        val isFBWTFormat = book.bookFormat == FictionBook.Format.FBWT
        itemView.tvFormat.isVisible = isFBWTFormat
    }

    private fun onBookMenuClicked(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.setOnMenuItemClickListener(menuListener)
        popupMenu.inflate(R.menu.menu_book_item)
        popupMenu.show()
    }

    private fun onBookClicked() {
        listener.onBookClicked(adapterPosition)
    }
}