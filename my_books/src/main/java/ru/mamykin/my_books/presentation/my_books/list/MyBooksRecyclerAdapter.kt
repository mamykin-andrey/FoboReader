package ru.mamykin.my_books.presentation.my_books.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.core.ui.adapterdelegates.AdapterDelegatesManager
import ru.mamykin.my_books.domain.model.BookInfo
import ru.mamykin.my_books.presentation.my_books.MyBooksViewModel

class MyBooksRecyclerAdapter(
        onAction: (MyBooksViewModel.Event) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var books: List<BookInfo> = listOf()
    private val delegatesManager = AdapterDelegatesManager<BookInfo>()

    init {
        delegatesManager.addDelegate(MyBookDelegate(onAction))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegatesManager.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            delegatesManager.bindViewHolder(books, holder, position)

    override fun getItemCount(): Int = books.size

    override fun getItemViewType(position: Int): Int =
            delegatesManager.getItemViewType(books, position)

    fun changeData(books: List<BookInfo>) {
        this.books = books
        notifyDataSetChanged()
    }
}