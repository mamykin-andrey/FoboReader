package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsRepository
import ru.mamykin.foboreader.settings.domain.model.SettingsItem
import javax.inject.Inject

internal class GetSettings @Inject constructor(
    private val settingsRepository: AppSettingsRepository,
    private val getSelectedLanguage: GetSelectedLanguage,
) {
    fun execute(): List<SettingsItem> {
        val selectedLanguageName = getSelectedLanguage.execute().name
        return with(settingsRepository) {
            listOf(
                SettingsItem.NightTheme(nightThemeEnabled),
                SettingsItem.Brightness(brightness),
                SettingsItem.ReadTextSize(readTextSize),
                SettingsItem.TranslationColor(translationColor),
                SettingsItem.AppLanguage(selectedLanguageName),
                SettingsItem.UseVibration(useVibration),
            )
        }
    }
}