package ru.mamykin.foboreader.core.data.storage

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.util.*

class AppSettingsStorage(
    private val prefManager: PreferencesManager
) {
    companion object {

        private const val NIGHT_THEME_ENABLED = "night_theme_enabled"
        private const val BRIGHTNESS = "brightness"
        private const val READ_TEXT_SIZE = "read_text_size"
        private const val TRANSLATION_COLOR = "translation_color"
        private const val APP_LANGUAGE_CODE = "app_language"
        private const val USE_VIBRATION = "use_vibration"
    }

    abstract class ObservableField<T> {

        private val channel = ConflatedBroadcastChannel(get())
        val flow: Flow<T> = channel.asFlow()

        abstract fun get(): T

        open fun set(value: T) {
            channel.offer(value)
        }
    }

    val nightThemeField = object : ObservableField<Boolean>() {
        override fun get(): Boolean {
            return prefManager.getBoolean(NIGHT_THEME_ENABLED, false)
        }

        override fun set(value: Boolean) {
            prefManager.putBoolean(NIGHT_THEME_ENABLED, value)
            super.set(value)
        }
    }

    val brightnessField = object : ObservableField<Int>() {
        override fun get(): Int {
            return prefManager.getInt(BRIGHTNESS) ?: 100
        }

        override fun set(value: Int) {
            prefManager.putInt(BRIGHTNESS, value)
            super.set(value)
        }
    }

    val readTextSizeField = object : ObservableField<Int>() {
        override fun get(): Int {
            return prefManager.getInt(READ_TEXT_SIZE) ?: 16
        }

        override fun set(value: Int) {
            value.takeIf { it in 10..30 }?.let {
                prefManager.putInt(READ_TEXT_SIZE, value)
            }
            super.set(value)
        }
    }

    val translationColorCodeField = object : ObservableField<String>() {
        override fun get(): String {
            return prefManager.getString(TRANSLATION_COLOR) ?: "#000000"
        }

        override fun set(value: String) {
            prefManager.putString(TRANSLATION_COLOR, value)
            super.set(value)
        }
    }

    val appLanguageField = object : ObservableField<String>() {
        override fun get(): String {
            return prefManager.getString(APP_LANGUAGE_CODE) ?: Locale.getDefault().language
        }

        override fun set(value: String) {
            prefManager.putString(APP_LANGUAGE_CODE, value)
            super.set(value)
        }
    }

    val useVibrationField = object : ObservableField<Boolean>() {
        override fun get(): Boolean {
            return prefManager.getBoolean(USE_VIBRATION, true)
        }

        override fun set(value: Boolean) {
            prefManager.putBoolean(USE_VIBRATION, value)
            super.set(value)
        }
    }
}