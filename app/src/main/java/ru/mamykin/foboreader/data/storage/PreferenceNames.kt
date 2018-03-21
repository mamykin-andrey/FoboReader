package ru.mamykin.foboreader.data.storage

/**
 * Creation date: 5/30/2017
 * Creation time: 2:45 PM
 * @author Andrey Mamykin(mamykin_av)
 */
interface PreferenceNames {
    companion object {
        const val PREFERENCES_FILE = "foboreader"
        const val NIGHT_THEME_PREF = "night_theme"
        const val BRIGHTNESS_PREF = "brightness"
        const val BRIGHTNESS_AUTO_PREF = "brightness_enabled"
        const val DROPBOX_TOKEN_PREF = "access_token"
        const val DROPBOX_LOGOUT_PREF = "dropbox_logout"
        const val DROPBOX_EMAIL_PREF = "dropbox_email"
        const val CONTENT_TEXT_SIZE_PREF = "content_text_size"
    }
}