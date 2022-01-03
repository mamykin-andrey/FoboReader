package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

class SetUseVibration @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(param: Boolean) {
        return appSettings.useVibrationField.set(param)
    }
}