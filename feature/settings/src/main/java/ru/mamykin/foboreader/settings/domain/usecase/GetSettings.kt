package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.settings.domain.model.AppSettings
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