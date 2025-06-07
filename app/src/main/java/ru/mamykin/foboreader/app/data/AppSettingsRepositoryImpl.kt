package ru.mamykin.foboreader.app.data

import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import java.util.Locale
import javax.inject.Inject

internal class AppSettingsRepositoryImpl @Inject constructor(
    private val prefManager: PreferencesManager,
) : AppSettingsRepository {

    companion object {
        private const val KEY_NIGHT_THEME_ENABLED = "night_theme_enabled"
        private const val KEY_BRIGHTNESS = "brightness"
        private const val KEY_READ_TEXT_SIZE = "read_text_size"
        private const val KEY_TRANSLATION_COLOR = "translation_color"
        private const val KEY_BACKGROUND_COLOR = "background_color"
        private const val KEY_TEXT_COLOR = "text_color"
        private const val KEY_APP_LANGUAGE_CODE = "app_language"
        private const val KEY_USE_VIBRATION = "use_vibration"
        private const val DEFAULT_NIGHT_THEME_ENABLED = false
        private const val DEFAULT_BRIGHTNESS = 100
        private const val DEFAULT_READ_TEXT_SIZE = 18
        private const val DEFAULT_TRANSLATION_COLOR = "#000000"
        private const val DEFAULT_BACKGROUND_COLOR = "#ffffff"
        private const val DEFAULT_TEXT_COLOR = "#000000"
        private const val DEFAULT_USE_VIBRATION = true
    }

    override fun isNightThemeEnabled(): Boolean {
        return prefManager.getBoolean(KEY_NIGHT_THEME_ENABLED, DEFAULT_NIGHT_THEME_ENABLED)
    }

    override fun setNightThemeEnabled(enabled: Boolean) {
        prefManager.putBoolean(KEY_NIGHT_THEME_ENABLED, enabled)
    }

    override fun getBrightness(): Int {
        return prefManager.getInt(KEY_BRIGHTNESS, DEFAULT_BRIGHTNESS)
    }

    override fun setBrightness(brightness: Int) {
        prefManager.putInt(KEY_BRIGHTNESS, brightness)
    }

    override fun getReadTextSize(): Int {
        return prefManager.getInt(KEY_READ_TEXT_SIZE, DEFAULT_READ_TEXT_SIZE)
    }

    override fun setReadTextSize(size: Int) {
        prefManager.putInt(KEY_READ_TEXT_SIZE, size)
    }

    override fun getTranslationColor(): String {
        return prefManager.getString(KEY_TRANSLATION_COLOR, DEFAULT_TRANSLATION_COLOR)
    }

    override fun setTranslationColor(color: String) {
        prefManager.putString(KEY_TRANSLATION_COLOR, color)
    }

    override fun getBackgroundColor(): String {
        return prefManager.getString(KEY_BACKGROUND_COLOR, DEFAULT_BACKGROUND_COLOR)
    }

    override fun setBackgroundColor(color: String) {
        prefManager.putString(KEY_BACKGROUND_COLOR, color)
    }

    override fun getTextColor(): String {
        return prefManager.getString(KEY_TEXT_COLOR, DEFAULT_TEXT_COLOR)
    }

    override fun setTextColor(color: String) {
        prefManager.putString(KEY_TEXT_COLOR, color)
    }

    override fun getAppLanguageCode(): String {
        return prefManager.getString(KEY_APP_LANGUAGE_CODE, Locale.getDefault().language)
    }

    override fun setAppLanguageCode(code: String) {
        prefManager.putString(KEY_APP_LANGUAGE_CODE, code)
    }

    override fun isUseVibration(): Boolean {
        return prefManager.getBoolean(KEY_USE_VIBRATION, DEFAULT_USE_VIBRATION)
    }

    override fun setUseVibration(use: Boolean) {
        prefManager.putBoolean(KEY_USE_VIBRATION, use)
    }
}