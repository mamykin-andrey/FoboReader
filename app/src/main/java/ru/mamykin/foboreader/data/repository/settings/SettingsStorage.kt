package ru.mamykin.foboreader.data.repository.settings

import ru.mamykin.foboreader.data.storage.PreferencesManager
import javax.inject.Inject

class SettingsStorage @Inject constructor(
        private val prefManager: PreferencesManager
) {

    companion object {
        const val NIGHT_THEME_PREF = "night_theme"
        const val BRIGHTNESS_VALUE_PREF = "brightness"
        const val MANUAL_BRIGHTNESS_PREF = "brightness_enabled"
        const val DROPBOX_ACCOUNT_PREF = "dropbox_email"
        const val CONTENT_TEXT_SIZE_PREF = "content_text_size"
    }

    var nightThemeEnabled: Boolean
        get() = prefManager.getBoolean(NIGHT_THEME_PREF, false)
        set(value) = prefManager.putBoolean(NIGHT_THEME_PREF, value)

    var manualBrightnessEnabled: Boolean
        get() = prefManager.getBoolean(MANUAL_BRIGHTNESS_PREF, false)
        set(value) = prefManager.putBoolean(MANUAL_BRIGHTNESS_PREF, value)

    var manualBrightnessValue: Float
        get() = prefManager.getFloat(BRIGHTNESS_VALUE_PREF, 1f)
        set(value) = prefManager.putFloat(BRIGHTNESS_VALUE_PREF, value)

    var bookTextSize: Int
        get() = prefManager.getInt(CONTENT_TEXT_SIZE_PREF, 16)
        set(value) = prefManager.putInt(CONTENT_TEXT_SIZE_PREF, value)

    var dropboxAccount: String?
        get() = prefManager.getString(DROPBOX_ACCOUNT_PREF, null)
        set(value) = prefManager.putString(DROPBOX_ACCOUNT_PREF, value)
}