package ru.mamykin.foboreader.settings.all_settings

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import javax.inject.Inject

internal class SetBrightnessUseCase @Inject constructor(
    private val appSettings: AppSettingsRepository
) {
    fun execute(value: Int) {
        appSettings.setBrightness(value)
    }
}