package ru.mamykin.foboreader.core.data.storage

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSettingsStorage @Inject constructor(
    private val prefManager: PreferencesManager,
) {
    companion object {

        private const val NIGHT_THEME_ENABLED = "night_theme_enabled"
        private const val BRIGHTNESS = "brightness"
        private const val READ_TEXT_SIZE = "read_text_size"
        private const val TRANSLATION_COLOR = "translation_color"
        private const val APP_LANGUAGE_CODE = "app_language"
        private const val USE_VIBRATION = "use_vibration"
    }

    fun nightThemeFlow() = prefManager.observeBooleanChanges(NIGHT_THEME_ENABLED)

    fun appLanguageFlow() = prefManager.observeStringChanges(APP_LANGUAGE_CODE)

    var nightThemeEnabled: Boolean
        get() = prefManager.getBoolean(NIGHT_THEME_ENABLED, false)
        set(value) {
            prefManager.putBoolean(NIGHT_THEME_ENABLED, value)
        }

    var brightness: Int
        get() = prefManager.getInt(BRIGHTNESS) ?: 100
        set(value) {
            prefManager.putInt(BRIGHTNESS, value)
        }

    var readTextSize: Int
        get() = prefManager.getInt(READ_TEXT_SIZE) ?: 16
        set(value) {
            value.takeIf { it in 10..30 }?.let {
                prefManager.putInt(READ_TEXT_SIZE, value)
            }
        }

    var translationColor: String
        get() = prefManager.getString(TRANSLATION_COLOR) ?: "#000000"
        set(value) {
            prefManager.putString(TRANSLATION_COLOR, value)
        }

    var appLanguageCode: String
        get() = prefManager.getString(APP_LANGUAGE_CODE) ?: Locale.getDefault().language
        set(value) {
            prefManager.putString(APP_LANGUAGE_CODE, value)
        }

    var useVibration: Boolean
        get() = prefManager.getBoolean(USE_VIBRATION, true)
        set(value) {
            prefManager.putBoolean(USE_VIBRATION, value)
        }
}