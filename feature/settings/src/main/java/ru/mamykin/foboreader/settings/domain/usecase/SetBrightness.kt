package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.UseCase

class SetBrightness(
    private val appSettings: AppSettingsStorage
) : UseCase<Int?, Unit>() {

    override fun execute(param: Int?) {
        appSettings.brightnessField.set(param)
    }
}