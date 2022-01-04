package ru.mamykin.foboreader.core.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

class GetVibrationEnabled @Inject constructor(
    private val settingsStorage: AppSettingsStorage
) {
    fun execute(): Boolean {
        return settingsStorage.useVibration
    }
}