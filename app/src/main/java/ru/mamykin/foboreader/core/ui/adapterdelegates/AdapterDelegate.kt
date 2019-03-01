package ru.mamykin.foboreader.core.ui.adapterdelegates

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class AdapterDelegate<H, T> {

    abstract fun isForViewType(item: T): Boolean

    abstract fun getLayoutId(): Int

    abstract fun createViewHolder(itemView: View): RecyclerView.ViewHolder

    abstract fun bindViewHolder(holder: RecyclerView.ViewHolder, item: T)
}