package ru.mamykin.foboreader.ui.store.list

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.squareup.picasso.Picasso

import java.util.ArrayList

import javax.inject.Inject

import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.common.UiUtils
import ru.mamykin.foboreader.data.model.StoreBook

/**
 * Адаптер с книгами на странице "Магазин"
 */
class BooksStoreRecyclerAdapter : RecyclerView.Adapter<StoreBookViewHolder>() {
    private var booksList: List<StoreBook>? = null
    @Inject
    lateinit var context: Context

    init {
        booksList = ArrayList()
        ReaderApp.component.inject(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreBookViewHolder {
        val view = LayoutInflater.from(
                parent.context).inflate(R.layout.item_store_book, parent, false)
        return StoreBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreBookViewHolder, position: Int) {
        val book = getItem(position)
        holder.tvBookName!!.text = book.title
        holder.tvBookAuthor!!.text = book.author
        holder.tvBookCategory!!.text = book.genre
        holder.tvRating!!.text = book.ratingStr
        holder.tvBookPrice!!.text = book.price
        holder.tvBookOldPrice!!.text = book.oldPrice
        UiUtils.setVisibility(holder.tvBookOldPrice!!, book.oldPrice != null)
        holder.tvBookOldPrice!!.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        Picasso.with(context).load(book.pictureUrl).placeholder(R.drawable.img_no_image).into(holder.ivCover)
    }

    override fun getItemCount(): Int {
        return booksList!!.size
    }

    fun getItem(position: Int): StoreBook {
        return booksList!![position]
    }

    fun changeData(booksList: List<StoreBook>) {
        this.booksList = booksList
        notifyDataSetChanged()
    }
}