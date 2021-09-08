package ru.mamykin.foboreader.core.di.api

import ru.mamykin.foboreader.core.data.storage.AppSettingsStorage

// todo: move it to CommonApi
interface SettingsApi {

    fun appSettingsStorage(): AppSettingsStorage
}