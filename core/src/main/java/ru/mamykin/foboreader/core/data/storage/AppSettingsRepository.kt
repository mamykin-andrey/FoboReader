package ru.mamykin.foboreader.core.data.storage

import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {

    fun nightThemeFlow(): Flow<Boolean>
    fun appLanguageFlow(): Flow<String>
    var nightThemeEnabled: Boolean
    var brightness: Int
    var readTextSize: Int
    var translationColor: String
    var appLanguageCode: String
    var useVibration: Boolean
}