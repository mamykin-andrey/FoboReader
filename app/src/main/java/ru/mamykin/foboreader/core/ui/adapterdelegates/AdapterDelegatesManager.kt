package ru.mamykin.foboreader.core.ui.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class AdapterDelegatesManager<T> {

    private val delegates: HashMap<Int, AdapterDelegate<T>> = hashMapOf()

    fun addDelegate(delegate: AdapterDelegate<T>) {
        delegates[delegates.size + 1] = delegate
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val delegate = getDelegateForViewType(viewType)
        val itemView = LayoutInflater.from(parent.context).inflate(delegate.getLayoutId(), parent, false)
        return delegate.createViewHolder(itemView)
    }

    fun bindViewHolder(items: List<T>, holder: RecyclerView.ViewHolder, position: Int) {
        val delegate = getDelegateForViewType(getItemViewType(items, position))
        delegate.bindViewHolder(holder, items[position])
    }

    fun getItemViewType(items: List<T>, position: Int): Int {
        return delegates.entries.find { it.value.isForViewType(items[position]) }?.key
                ?: throw IllegalStateException("Unknown viewType for position: $position!")

    }

    private fun getDelegateForViewType(viewType: Int): AdapterDelegate<T> {
        return delegates[viewType]
                ?: throw IllegalStateException("No viewType was found: $viewType!")
    }
}