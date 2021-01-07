package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.UseCase
import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages

class GetSelectedLanguage(
    private val appSettings: AppSettingsStorage
) : UseCase<Unit, Pair<String, String>>() {

    override fun execute(param: Unit): Pair<String, String> {
        // TODO: refactor this piece of shit
        val selectedLanguageCode = appSettings.appLanguageField.get()
        return supportedAppLanguages.find { it.second == selectedLanguageCode } ?: supportedAppLanguages.first()
    }
}