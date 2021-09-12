package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages
import javax.inject.Inject

class GetSelectedLanguage @Inject constructor(
    private val appSettings: AppSettingsStorage
) : UseCase<Unit, Pair<String, String>>() {

    override fun execute(param: Unit): Pair<String, String> {
        val selectedLanguageCode = appSettings.appLanguageField.get()
        return supportedAppLanguages.find { it.second == selectedLanguageCode } ?: supportedAppLanguages.first()
    }
}