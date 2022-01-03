package ru.mamykin.foboreader.settings.domain.usecase

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.settings.domain.model.supportedAppLanguages
import javax.inject.Inject

class GetSelectedLanguage @Inject constructor(
    private val appSettings: AppSettingsStorage
) {
    fun execute(): Result<Pair<String, String>> {
        return runCatching {
            Result.success(run {
                val selectedLanguageCode = appSettings.appLanguageField.get()
                supportedAppLanguages.find { it.second == selectedLanguageCode } ?: supportedAppLanguages.first()
            })
        }.getOrElse { Result.error(it) }
    }
}