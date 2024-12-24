package ru.mamykin.foboreader.core.di.api

interface ApiHolder {

    fun networkApi(): NetworkApi

    fun settingsApi(): SettingsApi

    fun mainApi(): MainApi

    fun commonApi(): CommonApi
}