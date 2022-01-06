package ru.mamykin.foboreader.settings.presentation.list

import ru.mamykin.foboreader.settings.databinding.ItemTextSizeBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.Settings

internal class TextSizeHolder(
    private val binding: ItemTextSizeBinding,
    private val onEvent: (Settings.Event) -> Unit,
) : SettingsItemHolder<SettingsItem.ReadTextSize>(binding.root) {

    init {
        binding.btnTextSizePlus.setOnClickListener {
            onEvent(Settings.Event.DecreaseTextSizeClicked)
        }

        binding.btnTextSizeMinus.setOnClickListener {
            onEvent(Settings.Event.IncreaseTextSizeClicked)
        }
    }

    override fun bind(item: SettingsItem.ReadTextSize) {
        binding.tvTextSize.text = item.textSize.toString()
    }
}