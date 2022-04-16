package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.AppSettingsRepository
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
                SettingsItem.NightTheme(isNightThemeEnabled()),
                SettingsItem.Brightness(getBrightness()),
                SettingsItem.ReadTextSize(getReadTextSize()),
                SettingsItem.TranslationColor(getTranslationColor()),
                SettingsItem.AppLanguage(selectedLanguageName),
                SettingsItem.UseVibration(isUseVibration()),
            )
        }
    }
}