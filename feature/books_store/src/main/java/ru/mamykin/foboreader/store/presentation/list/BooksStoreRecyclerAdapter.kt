package ru.mamykin.foboreader.store.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegatesManager
import ru.mamykin.foboreader.store.domain.model.StoreBook

class BooksStoreRecyclerAdapter(
    onBookClicked: (StoreBook) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Any> = listOf()

    private val delegatesManager = AdapterDelegatesManager<Any>()

    init {
        delegatesManager.addDelegate(StoreBookDelegate(onBookClicked))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegatesManager.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.bindViewHolder(items, holder, position)
    }

    override fun getItemCount(): Int = items.count()

    override fun getItemViewType(position: Int): Int =
        delegatesManager.getItemViewType(items, position)

    fun changeItems(items: List<Any>) {
        this.items = items
        notifyDataSetChanged()
    }
}