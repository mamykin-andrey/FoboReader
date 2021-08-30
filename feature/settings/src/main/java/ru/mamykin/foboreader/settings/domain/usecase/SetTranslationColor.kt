package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.UseCase
import javax.inject.Inject

class SetTranslationColor @Inject constructor(
    private val appSettingsStorage: AppSettingsStorage
) : UseCase<String, Unit>() {

    override fun execute(param: String) {
        appSettingsStorage.translationColorCodeField.set(param)
    }
}