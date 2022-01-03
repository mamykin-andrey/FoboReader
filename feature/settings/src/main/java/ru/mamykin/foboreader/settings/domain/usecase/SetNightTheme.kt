package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import javax.inject.Inject

class SetNightTheme @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(param: Boolean) {
        appSettings.nightThemeField.set(param)
    }
}