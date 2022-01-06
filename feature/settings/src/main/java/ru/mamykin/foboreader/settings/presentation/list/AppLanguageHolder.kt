package ru.mamykin.foboreader.settings.presentation.list

import ru.mamykin.foboreader.settings.databinding.ItemAppLanguageBinding
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import ru.mamykin.foboreader.settings.presentation.Settings

internal class AppLanguageHolder(
    private val binding: ItemAppLanguageBinding,
    private val onEvent: (Settings.Event) -> Unit,
) : SettingsItemHolder<SettingsItem.AppLanguage>(binding.root) {

    init {
        binding.clRoot.setOnClickListener {
            onEvent(Settings.Event.SelectAppLanguage)
        }
    }

    override fun bind(item: SettingsItem.AppLanguage) {
        binding.tvAppLanguageSubtitle.text = item.languageCode
    }
}