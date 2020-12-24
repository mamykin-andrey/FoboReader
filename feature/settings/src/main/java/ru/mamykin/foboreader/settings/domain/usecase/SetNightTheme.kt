package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.UseCase

class SetNightTheme(
    private val appSettings: AppSettingsStorage
) : UseCase<Boolean, Unit>() {

    override fun execute(param: Boolean) {
        appSettings.nightThemeField.set(param)
    }
}