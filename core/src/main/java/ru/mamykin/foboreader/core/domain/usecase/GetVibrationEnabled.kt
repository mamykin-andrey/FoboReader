package ru.mamykin.foboreader.core.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.UseCase

class GetVibrationEnabled(
    private val settingsStorage: AppSettingsStorage
) : UseCase<Unit, Boolean>() {

    override fun execute(param: Unit): Boolean {
        return settingsStorage.useVibrationField.get()
    }
}