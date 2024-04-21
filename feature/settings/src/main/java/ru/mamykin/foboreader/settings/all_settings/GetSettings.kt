package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.settings.all_settings.AppSettings
import ru.mamykin.foboreader.settings.app_language.GetSelectedLanguage
import javax.inject.Inject

internal class GetSettings @Inject constructor(
    private val settingsRepository: AppSettingsRepository,
    private val getSelectedLanguage: GetSelectedLanguage,
) {
    fun execute(): AppSettings {
        val selectedLanguageName = getSelectedLanguage.execute().name
        return with(settingsRepository) {
            AppSettings(
                isNightThemeEnabled = isNightThemeEnabled(),
                backgroundColor = getTranslationColor(),
                translationColor = getTranslationColor(),
                textSize = getReadTextSize(),
                languageName = selectedLanguageName,
                isVibrationEnabled = isUseVibration(),
            )
        }
    }
}