package ru.mamykin.foboreader.core.di.api

interface ApiHolder {

    fun navigationApi(): NavigationApi

    fun networkApi(): NetworkApi

    fun settingsApi(): SettingsApi

    fun mainApi(): MainApi
}