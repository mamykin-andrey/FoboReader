package ru.mamykin.foboreader.store.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.core.extension.getLayoutInflater
import ru.mamykin.foboreader.core.extension.setTextOrGone
import ru.mamykin.foboreader.core.presentation.list.SimpleDiffUtil
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.databinding.ItemCategoryBinding
import ru.mamykin.foboreader.store.domain.model.BookCategory

internal class CategoryListAdapter(
    private val onClick: (id: String) -> Unit,
) : ListAdapter<BookCategory, CategoryListAdapter.CategoryViewHolder>(
    SimpleDiffUtil.equalsCallback { it.id }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CategoryViewHolder(
        ItemCategoryBinding.inflate(parent.getLayoutInflater(), parent, false)
    ) { onClick(getItem(it).id) }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    internal class CategoryViewHolder(
        private val binding: ItemCategoryBinding,
        private val onClick: (pos: Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private val context = binding.root.context

        init {
            binding.root.setOnClickListener { onClick(absoluteAdapterPosition) }
        }

        fun bind(category: BookCategory) = binding.apply {
            tvName.text = category.name
            tvDescription.setTextOrGone(category.description)
            tvBooksCount.text = context.getString(R.string.books_store_category_count, category.booksCount)
        }
    }
}