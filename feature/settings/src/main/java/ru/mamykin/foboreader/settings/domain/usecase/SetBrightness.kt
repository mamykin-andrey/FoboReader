package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

internal class SetBrightness @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(value: Int) {
        appSettings.brightness = value
    }
}