package ru.mamykin.foboreader.app.domain

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

internal class GetVibrationEnabled @Inject constructor(
    private val settingsStorage: AppSettingsStorage
) {
    fun execute(): Boolean {
        return settingsStorage.useVibration
    }
}