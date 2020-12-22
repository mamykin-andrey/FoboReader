package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage

class SetBrightness(
    private val appSettings: AppSettingsStorage
) {
    operator fun invoke(value: Int?) {
        appSettings.brightnessField.set(value)
    }
}