package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SetUseVibration @Inject constructor(
    private val appSettings: AppSettingsRepository
) {
    fun execute(enabled: Boolean) {
        appSettings.setUseVibration(enabled)
    }
}