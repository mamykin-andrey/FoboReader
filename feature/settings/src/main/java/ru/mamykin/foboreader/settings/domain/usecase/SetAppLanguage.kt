package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.UseCase

class SetAppLanguage(
    private val appSettings: AppSettingsStorage
) : UseCase<String, Unit>() {

    override fun execute(param: String) {
        appSettings.appLanguageField.set(param)
    }
}