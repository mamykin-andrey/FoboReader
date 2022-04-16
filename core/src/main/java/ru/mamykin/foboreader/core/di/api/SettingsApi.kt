package ru.mamykin.foboreader.core.di.api

import ru.mamykin.foboreader.core.data.AppSettingsRepository

interface SettingsApi {

    fun appSettingsStorage(): AppSettingsRepository
}