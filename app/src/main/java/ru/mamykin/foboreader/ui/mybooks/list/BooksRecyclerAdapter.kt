package ru.mamykin.foboreader.ui.mybooks.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.data.model.FictionBook

class BooksRecyclerAdapter(
        private val listener: OnBookClickListener
) : RecyclerView.Adapter<BookViewHolder>() {

    private var books: List<FictionBook> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val book = LayoutInflater.from(
                parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(book, listener)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun changeData(books: List<FictionBook>) {
        this.books = books
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): FictionBook {
        return books[position]
    }

    interface OnBookClickListener {

        fun onBookClicked(position: Int)

        fun onBookAboutClicked(position: Int)

        fun onBookShareClicked(position: Int)

        fun onBookRemoveClicked(position: Int)
    }
}