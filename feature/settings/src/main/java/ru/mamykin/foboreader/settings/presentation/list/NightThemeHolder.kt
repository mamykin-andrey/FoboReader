package ru.mamykin.foboreader.settings.presentation.list

import ru.mamykin.foboreader.core.extension.setCheckedChangedListener
import ru.mamykin.foboreader.settings.databinding.ItemNightThemeBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.SettingsFeature

internal class NightThemeHolder(
    private val binding: ItemNightThemeBinding,
    private val onEvent: (SettingsFeature.Intent) -> Unit,
) : SettingsItemHolder<SettingsItem.NightTheme>(binding.root) {

    init {
        binding.swNightTheme.setCheckedChangedListener {
            onEvent(SettingsFeature.Intent.ChangeNightTheme(it))
        }
    }

    override fun bind(item: SettingsItem.NightTheme) {
        binding.swNightTheme.isChecked = item.isEnabled
    }
}