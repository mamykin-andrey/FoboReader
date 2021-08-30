package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import javax.inject.Inject

class SetAppLanguage @Inject constructor(
    private val appSettings: AppSettingsStorage
) : UseCase<String, Unit>() {

    override fun execute(param: String) {
        appSettings.appLanguageField.set(param)
    }
}