package ru.mamykin.foboreader.core.ui.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AdapterDelegate<H, T> {

    abstract fun isForViewType(item: Any): Boolean

    abstract fun getLayoutId(): Int

    abstract fun createViewHolder(itemView: View): RecyclerView.ViewHolder

    abstract fun innerBindViewHolder(holder: H, item: T)

    fun bindViewHolder(holder: RecyclerView.ViewHolder, item: Any) =
            innerBindViewHolder(holder as H, item as T)
}