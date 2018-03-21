package ru.mamykin.foboreader.ui.mybooks.list

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.data.model.FictionBook
import java.util.*
import javax.inject.Inject

/**
 * Адаптер с книгами на странице "Мои книги"
 */
class BooksRecyclerAdapter(private val listener: OnBookClickListener) : RecyclerView.Adapter<BookViewHolder>() {
    private var booksList: List<FictionBook>? = null
    @Inject
    lateinit var context: Context

    init {
        this.booksList = ArrayList()
        ReaderApp.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val book = LayoutInflater.from(
                parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(book, listener)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)
        holder.ivBookCover!!.setImageBitmap(BitmapFactory
                .decodeResource(context!!.resources, R.drawable.img_no_image))
        holder.tvBookTitle!!.text = item.bookTitle
        holder.tvBookAuthor!!.text = item.bookAuthor
        holder.vProgress!!.setPercents(item.percents)
        holder.tvBookPages!!.text = item.pagesCountString
        holder.tvBookAddedDate!!.text = item.lastOpenString
        UiUtils.setVisibility(holder.vFormat!!, item.bookFormat == FictionBook.Format.FBWT)
    }

    override fun getItemCount(): Int {
        return booksList!!.size
    }

    fun getItem(position: Int): FictionBook {
        return booksList!![position]
    }

    fun changeData(booksList: List<FictionBook>) {
        this.booksList = booksList
        notifyDataSetChanged()
    }

    interface OnBookClickListener {
        fun onBookClicked(position: Int)

        fun onBookAboutClicked(position: Int)

        fun onBookShareClicked(position: Int)

        fun onBookRemoveClicked(position: Int)
    }
}