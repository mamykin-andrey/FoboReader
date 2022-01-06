package ru.mamykin.foboreader.settings.presentation.list

import ru.mamykin.foboreader.settings.databinding.ItemUseVibrationBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.Settings

internal class UseVibrationHolder(
    private val binding: ItemUseVibrationBinding,
    private val onEvent: (Settings.Event) -> Unit,
) : SettingsItemHolder<SettingsItem.UseVibration>(binding.root) {

    init {
        binding.clRoot.setOnClickListener {
            onEvent(Settings.Event.UseVibrationChanged(!binding.cbOptionValue.isChecked))
        }
    }

    override fun bind(item: SettingsItem.UseVibration) {
        binding.cbOptionValue.isChecked = item.enabled
    }
}