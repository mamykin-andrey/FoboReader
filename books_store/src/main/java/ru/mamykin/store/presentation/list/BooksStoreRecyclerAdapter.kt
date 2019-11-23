package ru.mamykin.store.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.core.ui.adapterdelegates.AdapterDelegatesManager

class BooksStoreRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Any> = listOf()

    private val delegatesManager = AdapterDelegatesManager<Any>()

    init {
        delegatesManager.addDelegate(StoreBookDelegate())
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