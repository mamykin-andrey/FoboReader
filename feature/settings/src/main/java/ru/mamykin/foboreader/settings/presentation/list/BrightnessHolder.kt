package ru.mamykin.foboreader.settings.presentation.list

import ru.mamykin.foboreader.core.extension.setProgressChangedListener
import ru.mamykin.foboreader.settings.databinding.ItemBackgroundBrightnessBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.SettingsFeature

internal class BrightnessHolder(
    private val binding: ItemBackgroundBrightnessBinding,
    private val onEvent: (SettingsFeature.Intent) -> Unit,
) : SettingsItemHolder<SettingsItem.Brightness>(binding.root) {

    init {
        binding.seekBright.setProgressChangedListener {
            onEvent(SettingsFeature.Intent.ChangeBrightness(it))
        }
    }

    override fun bind(item: SettingsItem.Brightness) {
        binding.seekBright.progress = item.brightness
    }
}