package ru.mamykin.core.ui.adapterdelegates

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class AdapterDelegate<T> {

    abstract fun isForViewType(item: T): Boolean

    abstract fun getLayoutId(): Int

    abstract fun createViewHolder(itemView: View): RecyclerView.ViewHolder

    abstract fun bindViewHolder(holder: RecyclerView.ViewHolder, item: T)
}