package ru.mamykin.foboreader.settings.app_language

import javax.inject.Inject

internal class GetAppLanguages @Inject constructor() {

    fun execute(): List<AppLanguage> {
        return supportedAppLanguages
    }
}