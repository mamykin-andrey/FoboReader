package ru.mamykin.foboreader.presentation.mybooks.list

import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_book.view.*
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.extension.isVisible
import ru.mamykin.foboreader.domain.entity.FictionBook

class BookViewHolder(itemView: View,
                     private val onBookClickFunc: (Int) -> Unit,
                     private val onBookAboutClickFunc: (Int) -> Unit,
                     private val onBookShareClickFunc: (Int) -> Unit,
                     private val onBookRemoveClickFunc: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val menuListener = PopupMenu.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.menu_about_book -> onBookAboutClickFunc(adapterPosition)
            R.id.menu_share_book -> onBookShareClickFunc(adapterPosition)
            R.id.menu_remove_book -> onBookRemoveClickFunc(adapterPosition)
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
        itemView.tvBookTitle.text = book.bookTitle
        itemView.tvAuthor.text = book.bookAuthor
        itemView.pvProgress.setPercentage(book.readPercent)
        itemView.tvBooksPages.text = book.pagesCountString
        itemView.tvBookAddedDate.text = book.lastOpenString
        itemView.tvFormat.isVisible = book.isFbWtBook
    }

    private fun onBookMenuClicked(view: View) {
        val popupMenu = PopupMenu(view.context, view)
        popupMenu.setOnMenuItemClickListener(menuListener)
        popupMenu.inflate(R.menu.menu_book_item)
        popupMenu.show()
    }

    private fun onBookClicked() {
        onBookClickFunc(adapterPosition)
    }
}