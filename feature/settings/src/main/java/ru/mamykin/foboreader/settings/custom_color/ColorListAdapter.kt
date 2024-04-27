package ru.mamykin.foboreader.settings.custom_color

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.core.extension.getLayoutInflater
import ru.mamykin.foboreader.core.presentation.list.SimpleDiffUtil
import ru.mamykin.foboreader.settings.databinding.ItemColorBinding

internal class ColorListAdapter(
    private val onClick: (String) -> Unit,
) : ListAdapter<ColorItem, ColorListAdapter.ColorItemViewHolder>(
    SimpleDiffUtil.equalsCallback { it.colorCode }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ColorItemViewHolder(
        ItemColorBinding.inflate(parent.getLayoutInflater(), parent, false)
    ) { onClick(getItem(it).colorCode) }

    override fun onBindViewHolder(holder: ColorItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    internal class ColorItemViewHolder(
        private val binding: ItemColorBinding,
        private val onClick: (Int) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClick(absoluteAdapterPosition)
            }
        }

        fun bind(item: ColorItem) = binding.apply {
            vColorItem.background.colorFilter = PorterDuffColorFilter(
                Color.parseColor(item.colorCode),
                PorterDuff.Mode.SRC_ATOP
            )
            ivIsSelected.isVisible = item.selected
            ivIsSelected.colorFilter = PorterDuffColorFilter(
                Color.parseColor(item.checkColorCode),
                PorterDuff.Mode.SRC_ATOP
            )
        }
    }
}