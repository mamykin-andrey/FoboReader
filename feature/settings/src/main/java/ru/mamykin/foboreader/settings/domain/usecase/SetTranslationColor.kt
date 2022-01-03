package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import javax.inject.Inject

class SetTranslationColor @Inject constructor(
    private val appSettingsStorage: AppSettingsStorage
) {
    fun execute(param: String): Result<Unit> {
        return runCatching {
            Result.success(appSettingsStorage.translationColorCodeField.set(param))
        }.getOrElse { Result.error(it) }
    }
}