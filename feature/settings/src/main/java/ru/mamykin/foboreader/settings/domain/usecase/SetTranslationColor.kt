package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

class SetTranslationColor @Inject constructor(
    private val appSettingsStorage: AppSettingsStorage
) {
    fun execute(param: String) {
        appSettingsStorage.translationColorCodeField.set(param)
    }
}