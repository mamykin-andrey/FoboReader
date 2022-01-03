package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import javax.inject.Inject

class SetAppLanguage @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(languageCode: String): Result<Unit> {
        return runCatching {
            Result.success(appSettings.appLanguageField.set(languageCode))
        }.getOrElse { Result.error(it) }
    }
}