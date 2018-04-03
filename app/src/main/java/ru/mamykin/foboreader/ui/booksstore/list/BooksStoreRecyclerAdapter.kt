package ru.mamykin.foboreader.ui.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.entity.StoreBook

class BooksStoreRecyclerAdapter : RecyclerView.Adapter<StoreBookViewHolder>() {

    private var books: List<StoreBook> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreBookViewHolder {
        val view = LayoutInflater.from(
                parent.context).inflate(R.layout.item_store_book, parent, false)
        return StoreBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoreBookViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    fun changeData(books: List<StoreBook>) {
        this.books = books
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): StoreBook {
        return books[position]
    }
}