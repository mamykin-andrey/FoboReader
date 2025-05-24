package ru.mamykin.foboreader.core.data

interface AppSettingsRepository {

    fun isNightThemeEnabled(): Boolean
    fun setNightThemeEnabled(enabled: Boolean)
    fun getBrightness(): Int
    fun setBrightness(brightness: Int)
    fun getReadTextSize(): Int
    fun setReadTextSize(size: Int)
    fun getTranslationColor(): String
    fun setTranslationColor(color: String)
    fun getBackgroundColor(): String
    fun setBackgroundColor(color: String)
    fun getTextColor(): String
    fun setTextColor(color: String)
    fun getAppLanguageCode(): String
    fun setAppLanguageCode(code: String)
    fun isUseVibration(): Boolean
    fun setUseVibration(use: Boolean)
}