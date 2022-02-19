package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

internal class GetVibrationEnabled @Inject constructor(
    private val settingsStorage: AppSettingsStorage
) {
    fun execute(): Boolean {
        return settingsStorage.useVibration
    }
}