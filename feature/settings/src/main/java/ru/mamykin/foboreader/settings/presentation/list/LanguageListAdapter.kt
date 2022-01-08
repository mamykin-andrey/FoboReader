package ru.mamykin.foboreader.settings.presentation.list

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.core.extension.getLayoutInflater
import ru.mamykin.foboreader.core.presentation.list.SimpleDiffUtil
import ru.mamykin.foboreader.settings.databinding.ItemLanguageBinding
import ru.mamykin.foboreader.settings.domain.model.AppLanguage

internal class LanguageListAdapter(
    private val onClick: (String) -> Unit,
) : ListAdapter<AppLanguage, LanguageListAdapter.LanguageViewHolder>(
    SimpleDiffUtil.equalsCallback { it.code }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LanguageViewHolder(
        ItemLanguageBinding.inflate(parent.getLayoutInflater(), parent, false)
    ) { onClick(getItem(it).code) }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    internal class LanguageViewHolder(
        private val binding: ItemLanguageBinding,
        private val onClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick(absoluteAdapterPosition)
            }
        }

        fun bind(item: AppLanguage) {
            binding.tvName.text = item.name
        }
    }
}