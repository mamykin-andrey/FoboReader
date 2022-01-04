package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages
import javax.inject.Inject

internal class GetSelectedLanguage @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(): Pair<String, String> {
        val selectedLanguageCode = appSettings.appLanguageCode
        return supportedAppLanguages.find { it.second == selectedLanguageCode }
            ?: supportedAppLanguages.first()
    }
}