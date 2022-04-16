package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SetAppLanguage @Inject constructor(
    private val appSettings: AppSettingsRepository
) {
    fun execute(languageCode: String) {
        appSettings.setAppLanguageCode(languageCode)
    }
}