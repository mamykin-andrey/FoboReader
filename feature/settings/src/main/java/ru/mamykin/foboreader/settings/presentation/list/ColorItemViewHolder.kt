package ru.mamykin.foboreader.settings.presentation.list

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import ru.mamykin.foboreader.settings.databinding.ItemColorBinding
import ru.mamykin.foboreader.settings.domain.model.ColorItem

class ColorItemViewHolder(
    private val binding: ItemColorBinding
) : RecyclerView.ViewHolder(binding.root) {

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