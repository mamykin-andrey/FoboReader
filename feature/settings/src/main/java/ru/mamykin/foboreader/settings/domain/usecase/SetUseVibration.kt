package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

internal class SetUseVibration @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(enabled: Boolean) {
        appSettings.useVibration = enabled
    }
}