package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.UseCase

class SetUseVibration(
    private val appSettings: AppSettingsStorage
) : UseCase<Boolean, Unit>() {

    override fun execute(param: Boolean) {
        appSettings.useVibrationField.set(param)
    }
}