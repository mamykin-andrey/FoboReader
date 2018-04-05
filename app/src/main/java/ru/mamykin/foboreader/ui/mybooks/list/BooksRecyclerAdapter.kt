package ru.mamykin.foboreader.ui.mybooks.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.FictionBook

class BooksRecyclerAdapter(
        private val listener: OnBookClickListener
) : RecyclerView.Adapter<BookViewHolder>() {

    private var books: List<FictionBook> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val book = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(
                book,
                this::onBookClicked,
                this::onBookAboutClicked,
                this::onBookShareClicked,
                this::onBookRemoveClicked
        )
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = books[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun changeData(books: List<FictionBook>) {
        this.books = books
        notifyDataSetChanged()
    }

    private fun onBookClicked(position: Int) {
        listener.onBookClicked(books[position].filePath)
    }

    private fun onBookAboutClicked(position: Int) {
        listener.onBookAboutClicked(books[position].filePath)
    }

    private fun onBookShareClicked(position: Int) {
        listener.onBookShareClicked(books[position].filePath)
    }

    private fun onBookRemoveClicked(position: Int) {
        listener.onBookRemoveClicked(books[position].filePath)
    }

    interface OnBookClickListener {

        fun onBookClicked(bookPath: String)

        fun onBookAboutClicked(bookPath: String)

        fun onBookShareClicked(bookPath: String)

        fun onBookRemoveClicked(bookPath: String)
    }
}