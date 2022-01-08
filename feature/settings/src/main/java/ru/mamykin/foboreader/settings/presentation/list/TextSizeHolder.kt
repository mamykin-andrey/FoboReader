package ru.mamykin.foboreader.settings.presentation.list

import ru.mamykin.foboreader.settings.databinding.ItemTextSizeBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.SettingsFeature

internal class TextSizeHolder(
    private val binding: ItemTextSizeBinding,
    private val onEvent: (SettingsFeature.Intent) -> Unit,
) : SettingsItemHolder<SettingsItem.ReadTextSize>(binding.root) {

    init {
        binding.btnTextSizePlus.setOnClickListener {
            onEvent(SettingsFeature.Intent.DecreaseTextSize)
        }

        binding.btnTextSizeMinus.setOnClickListener {
            onEvent(SettingsFeature.Intent.IncreaseTextSize)
        }
    }

    override fun bind(item: SettingsItem.ReadTextSize) {
        binding.tvTextSize.text = item.textSize.toString()
    }
}