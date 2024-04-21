package ru.mamykin.foboreader.settings.app_language

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class GetSelectedLanguage @Inject constructor(
    private val appSettings: AppSettingsRepository
) {
    fun execute(): AppLanguage {
        val selectedLanguageCode = appSettings.getAppLanguageCode()
        return supportedAppLanguages.find { it.code == selectedLanguageCode }
            ?: supportedAppLanguages.first()
    }
}