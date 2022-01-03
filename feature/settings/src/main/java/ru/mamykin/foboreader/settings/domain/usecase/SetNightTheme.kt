package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import javax.inject.Inject

class SetNightTheme @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(param: Boolean): Result<Unit> {
        return runCatching {
            Result.success(appSettings.nightThemeField.set(param))
        }.getOrElse { Result.error(it) }
    }
}