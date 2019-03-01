package ru.mamykin.foboreader.presentation.booksstore.list

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import ru.mamykin.foboreader.core.ui.adapterdelegates.AdapterDelegatesManager

class MainStoreRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Any> = listOf()

    private val delegatesManager = AdapterDelegatesManager<Any>()

    init {
        delegatesManager.addDelegate(StoreCategoryDelegate())
        delegatesManager.addDelegate(FeaturedBookDelegate())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            delegatesManager.createViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.bindViewHolder(items, holder, position)
    }

    override fun getItemCount(): Int = items.count()

    override fun getItemViewType(position: Int): Int =
            delegatesManager.getItemViewType(items, position)

    // TODO: DiffUtil
    fun changeItems(items: List<Any>) {
        this.items = items
        notifyDataSetChanged()
    }
}