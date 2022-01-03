package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import javax.inject.Inject

class SetBrightness @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(value: Int): Result<Unit> {
        return runCatching {
            Result.success(appSettings.brightnessField.set(value))
        }.getOrElse { Result.error(it) }
    }
}