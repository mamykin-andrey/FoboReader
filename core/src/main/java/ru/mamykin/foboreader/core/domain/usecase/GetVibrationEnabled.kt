package ru.mamykin.foboreader.core.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import javax.inject.Inject

class GetVibrationEnabled @Inject constructor(
    private val settingsStorage: AppSettingsStorage
) {
    fun execute(): Result<Boolean> {
        return runCatching {
            Result.success(settingsStorage.useVibrationField.get())
        }.getOrElse { Result.error(it) }
    }
}