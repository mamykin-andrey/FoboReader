package ru.mamykin.my_books.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.core.ui.adapterdelegates.AdapterDelegatesManager
import ru.mamykin.core.data.model.FictionBook

class MyBooksRecyclerAdapter(
        onBookClicked: (String) -> Unit,
        onBookAboutClicked: (String) -> Unit,
        onBookShareClicked: (String) -> Unit,
        onBookRemoveClicked: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var books: List<FictionBook> = listOf()
    private val delegatesManager = AdapterDelegatesManager<FictionBook>()

    init {
        delegatesManager.addDelegate(MyBookDelegate(
                onBookClicked,
                onBookAboutClicked,
                onBookShareClicked,
                onBookRemoveClicked
        ))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            delegatesManager.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
            delegatesManager.bindViewHolder(books, holder, position)

    override fun getItemCount(): Int = books.size

    override fun getItemViewType(position: Int): Int =
            delegatesManager.getItemViewType(books, position)

    fun changeData(books: List<FictionBook>) {
        this.books = books
        notifyDataSetChanged()
    }
}