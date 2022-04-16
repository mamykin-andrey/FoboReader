package ru.mamykin.foboreader.core.di.api

import ru.mamykin.foboreader.core.data.storage.AppSettingsRepository

interface SettingsApi {

    fun appSettingsStorage(): AppSettingsRepository
}