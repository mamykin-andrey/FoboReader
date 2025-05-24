package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.settings.app_language.AppLanguage
import ru.mamykin.foboreader.settings.app_language.supportedAppLanguages
import javax.inject.Inject

internal class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: AppSettingsRepository,
) {
    fun execute(): AppSettings {
        val selectedLanguageName = getSelectedLanguage().name
        return with(settingsRepository) {
            AppSettings(
                isNightThemeEnabled = isNightThemeEnabled(),
                backgroundColor = getBackgroundColor(),
                textColor = getTextColor(),
                translationColor = getTranslationColor(),
                textSize = getReadTextSize(),
                languageName = selectedLanguageName,
                isVibrationEnabled = isUseVibration(),
            )
        }
    }

    private fun getSelectedLanguage(): AppLanguage {
        val selectedLanguageCode = settingsRepository.getAppLanguageCode()
        return supportedAppLanguages.find { it.code == selectedLanguageCode }
            ?: supportedAppLanguages.first()
    }
}