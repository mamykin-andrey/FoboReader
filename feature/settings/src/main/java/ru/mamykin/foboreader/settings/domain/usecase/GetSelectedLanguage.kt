package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsRepository
import ru.mamykin.foboreader.settings.domain.model.AppLanguage
import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages
import javax.inject.Inject

internal class GetSelectedLanguage @Inject constructor(
    private val appSettings: AppSettingsRepository
) {
    fun execute(): AppLanguage {
        val selectedLanguageCode = appSettings.appLanguageCode
        return supportedAppLanguages.find { it.code == selectedLanguageCode }
            ?: supportedAppLanguages.first()
    }
}