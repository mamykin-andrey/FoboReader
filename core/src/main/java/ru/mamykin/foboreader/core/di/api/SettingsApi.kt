package ru.mamykin.foboreader.core.di.api

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage

interface SettingsApi {

    fun appSettingsStorage(): AppSettingsStorage
}