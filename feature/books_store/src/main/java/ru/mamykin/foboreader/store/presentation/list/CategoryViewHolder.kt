package ru.mamykin.foboreader.store.presentation.list

import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.core.extension.setTextOrGone
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.ItemCategoryBinding
import ru.mamykin.foboreader.store.domain.model.StoreBookCategory

internal class CategoryViewHolder(
    private val binding: ItemCategoryBinding
) : RecyclerView.ViewHolder(binding.root) {

    private val context = binding.root.context

    fun bind(category: StoreBookCategory) = binding.apply {
        tvName.text = category.name
        tvDescription.setTextOrGone(category.description)
        tvBooksCount.text = context.getString(R.string.books_store_category_count, category.booksCount)
    }
}