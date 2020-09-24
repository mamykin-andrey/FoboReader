package ru.mamykin.foboreader.core.ui.adapterdelegates

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AdapterDelegate<T> {

    abstract fun isForViewType(item: T): Boolean

    abstract fun getLayoutId(): Int

    abstract fun createViewHolder(itemView: View): RecyclerView.ViewHolder

    abstract fun bindViewHolder(holder: RecyclerView.ViewHolder, item: T)
}