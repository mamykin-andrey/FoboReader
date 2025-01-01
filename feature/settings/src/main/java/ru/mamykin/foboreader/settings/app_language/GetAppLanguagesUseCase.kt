package ru.mamykin.foboreader.settings.app_language

import javax.inject.Inject

internal class GetAppLanguagesUseCase @Inject constructor() {

    fun execute(): List<AppLanguage> {
        return supportedAppLanguages
    }
}