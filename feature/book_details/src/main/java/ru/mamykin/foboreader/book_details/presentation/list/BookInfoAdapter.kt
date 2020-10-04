package ru.mamykin.foboreader.book_details.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.book_details.presentation.model.BookInfoItem
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegatesManager

class BookInfoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var infoItems: List<BookInfoItem> = listOf()
    private val delegatesManager = AdapterDelegatesManager<BookInfoItem>()

    init {
        delegatesManager.addDelegate(BookInfoDelegate())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        delegatesManager.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        delegatesManager.bindViewHolder(infoItems, holder, position)

    override fun getItemCount(): Int = infoItems.size

    override fun getItemViewType(position: Int): Int =
        delegatesManager.getItemViewType(infoItems, position)

    fun changeData(infoItems: List<BookInfoItem>) {
        this.infoItems = infoItems
        notifyDataSetChanged()
    }
}