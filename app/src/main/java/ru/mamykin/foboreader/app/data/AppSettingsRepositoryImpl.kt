package ru.mamykin.foboreader.app.data

import kotlinx.coroutines.flow.Flow
import ru.mamykin.foboreader.core.data.AppSettingsRepository
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AppSettingsRepositoryImpl @Inject constructor(
    private val prefManager: PreferencesManager,
) : AppSettingsRepository {
    companion object {

        private const val NIGHT_THEME_ENABLED = "night_theme_enabled"
        private const val BRIGHTNESS = "brightness"
        private const val READ_TEXT_SIZE = "read_text_size"
        private const val TRANSLATION_COLOR = "translation_color"
        private const val APP_LANGUAGE_CODE = "app_language"
        private const val USE_VIBRATION = "use_vibration"
    }

    override fun nightThemeFlow(): Flow<Boolean> {
        return prefManager.observeBooleanChanges(NIGHT_THEME_ENABLED)
    }

    override fun appLanguageFlow(): Flow<String> {
        return prefManager.observeStringChanges(APP_LANGUAGE_CODE)
    }

    override fun isNightThemeEnabled(): Boolean {
        return prefManager.getBoolean(NIGHT_THEME_ENABLED, false)
    }

    override fun setNightThemeEnabled(enabled: Boolean) {
        prefManager.putBoolean(NIGHT_THEME_ENABLED, enabled)
    }

    override fun getBrightness(): Int {
        return prefManager.getInt(BRIGHTNESS) ?: 100
    }

    override fun setBrightness(brightness: Int) {
        prefManager.putInt(BRIGHTNESS, brightness)
    }

    override fun getReadTextSize(): Int {
        return prefManager.getInt(READ_TEXT_SIZE) ?: 16
    }

    override fun setReadTextSize(size: Int) {
        size.takeIf { it in 10..30 }?.let {
            prefManager.putInt(READ_TEXT_SIZE, size)
        }
    }

    override fun getTranslationColor(): String {
        return prefManager.getString(TRANSLATION_COLOR) ?: "#000000"
    }

    override fun setTranslationColor(color: String) {
        prefManager.putString(TRANSLATION_COLOR, color)
    }

    override fun getAppLanguageCode(): String {
        return prefManager.getString(APP_LANGUAGE_CODE) ?: Locale.getDefault().language
    }

    override fun setAppLanguageCode(code: String) {
        prefManager.putString(APP_LANGUAGE_CODE, code)
    }

    override fun isUseVibration(): Boolean {
        return prefManager.getBoolean(USE_VIBRATION, true)
    }

    override fun setUseVibration(use: Boolean) {
        prefManager.putBoolean(USE_VIBRATION, use)
    }
}