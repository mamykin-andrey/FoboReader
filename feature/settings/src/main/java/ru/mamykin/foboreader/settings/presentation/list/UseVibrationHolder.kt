package ru.mamykin.foboreader.settings.presentation.list

import ru.mamykin.foboreader.settings.databinding.ItemUseVibrationBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.SettingsFeature

internal class UseVibrationHolder(
    private val binding: ItemUseVibrationBinding,
    private val onEvent: (SettingsFeature.Intent) -> Unit,
) : SettingsItemHolder<SettingsItem.UseVibration>(binding.root) {

    init {
        binding.clRoot.setOnClickListener {
            onEvent(SettingsFeature.Intent.ChangeUseVibration(!binding.cbOptionValue.isChecked))
        }
    }

    override fun bind(item: SettingsItem.UseVibration) {
        binding.cbOptionValue.isChecked = item.enabled
    }
}