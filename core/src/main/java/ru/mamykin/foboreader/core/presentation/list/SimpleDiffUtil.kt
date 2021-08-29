package ru.mamykin.foboreader.core.presentation.list

import androidx.recyclerview.widget.DiffUtil

object SimpleDiffUtil {
    fun <T> equalsCallback(idSelector: (T) -> Any) = object : DiffUtil.ItemCallback<T>() {

        override fun areItemsTheSame(oldItem: T, newItem: T) = idSelector(oldItem) == idSelector(newItem)

        override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    }
}