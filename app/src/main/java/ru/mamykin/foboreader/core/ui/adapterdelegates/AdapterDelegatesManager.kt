package ru.mamykin.foboreader.core.ui.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class AdapterDelegatesManager {

    private val delegates: HashMap<Int, AdapterDelegate<*, *>> = hashMapOf()

    fun addDelegate(delegate: AdapterDelegate<*, *>) {
        delegates[delegates.size + 1] = delegate
    }

    fun createViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            getDelegateForViewType(viewType).createViewHolder(parent)

    fun onBindViewHolder(items: List<Any>, holder: RecyclerView.ViewHolder, position: Int) {
        val delegate = getDelegateForViewType(getItemViewType(items, position))
        delegate.bindViewHolder(holder, items[position])
    }

    fun getItemViewType(items: List<Any>, position: Int): Int {
        return delegates.entries.find { it.value.isForViewType(items[position]) }?.key
                ?: throw IllegalStateException("Unknown viewType for position: $position!")

    }

    private fun getDelegateForViewType(viewType: Int): AdapterDelegate<*, *> {
        return delegates[viewType]
                ?: throw IllegalStateException("No viewType was found: $viewType!")
    }
}