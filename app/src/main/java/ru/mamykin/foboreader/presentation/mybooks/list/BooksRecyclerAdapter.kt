package ru.mamykin.foboreader.presentation.mybooks.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegatesManager
import ru.mamykin.foboreader.domain.entity.FictionBook

class BooksRecyclerAdapter(
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

    override fun getItemCount(): Int {
        return books.size
    }

    fun changeData(books: List<FictionBook>) {
        this.books = books
        notifyDataSetChanged()
    }
}