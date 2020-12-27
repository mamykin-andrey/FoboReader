package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.UseCase

class SetTranslationColor(
    private val appSettingsStorage: AppSettingsStorage
) : UseCase<String, Unit>() {

    override fun execute(param: String) {
        appSettingsStorage.translationColorCodeField.set(param)
    }
}