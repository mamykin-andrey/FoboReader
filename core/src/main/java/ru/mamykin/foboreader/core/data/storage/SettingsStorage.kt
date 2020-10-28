package ru.mamykin.foboreader.core.data.storage

class SettingsStorage(
    private val prefManager: PreferencesManager
) {
    companion object {

        private const val NIGHT_THEME_ENABLED = "night_theme_enabled"
        private const val AUTO_BRIGHTNESS_ENABLED = "auto_brightness_enabled"
        private const val BRIGHTNESS = "brightness"
        private const val READ_TEXT_SIZE = "read_text_size"
    }

    var isNightTheme: Boolean
        get() = prefManager.getBoolean(NIGHT_THEME_ENABLED, false)
        set(value) = prefManager.putBoolean(NIGHT_THEME_ENABLED, value)

    var isAutoBrightness: Boolean
        get() = prefManager.getBoolean(AUTO_BRIGHTNESS_ENABLED, false)
        set(value) = prefManager.putBoolean(AUTO_BRIGHTNESS_ENABLED, value)

    var brightness: Int
        get() = prefManager.getInt(BRIGHTNESS) ?: 100
        set(value) = prefManager.putInt(BRIGHTNESS, value)

    var readTextSize: Int
        get() = prefManager.getInt(READ_TEXT_SIZE) ?: 16
        set(value) = prefManager.putInt(READ_TEXT_SIZE, value)
}