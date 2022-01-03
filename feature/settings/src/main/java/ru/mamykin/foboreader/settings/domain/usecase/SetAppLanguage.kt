package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

class SetAppLanguage @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(languageCode: String) {
        return appSettings.appLanguageField.set(languageCode)
    }
}