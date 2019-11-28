package ru.mamykin.core.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsStorage @Inject constructor(
        private val prefManager: PreferencesManager
) {
    companion object {
        private const val NIGHT_THEME_ENABLED = "night_theme_enabled"
        private const val MANUAL_BRIGHTNESS_ENABLED = "manual_brightness_enabled"
        private const val BRIGHTNESS = "brightness"
        private const val READ_TEXT_SIZE = "read_text_size"
    }

    var isNightTheme: Boolean
        get() = prefManager.getBoolean(NIGHT_THEME_ENABLED, false)
        set(value) = prefManager.putBoolean(NIGHT_THEME_ENABLED, value)

    var isManualBrightness: Boolean
        get() = prefManager.getBoolean(MANUAL_BRIGHTNESS_ENABLED, false)
        set(value) = prefManager.putBoolean(MANUAL_BRIGHTNESS_ENABLED, value)

    var brightness: Int
        get() = prefManager.getInt(BRIGHTNESS, 100)
        set(value) = prefManager.putInt(BRIGHTNESS, value)

    var readTextSize: Int
        get() = prefManager.getInt(READ_TEXT_SIZE, 16)
        set(value) = prefManager.putInt(READ_TEXT_SIZE, value)
}