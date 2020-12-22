package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage

class SetNightTheme(
    private val appSettings: AppSettingsStorage
) {
    operator fun invoke(enabled: Boolean) {
        appSettings.nightThemeField.set(enabled)
    }
}