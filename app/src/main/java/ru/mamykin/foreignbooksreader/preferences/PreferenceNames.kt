package ru.mamykin.foreignbooksreader.preferences

/**
 * Creation date: 5/30/2017
 * Creation time: 2:45 PM
 * @author Andrey Mamykin(mamykin_av)
 */
interface PreferenceNames {
    companion object {
        val PREFERENCES_FILE = "foboreader"
        val NIGHT_THEME_PREF = "night_theme"
        val BRIGHTNESS_PREF = "brightness"
        val BRIGHTNESS_AUTO_PREF = "brightness_enabled"
        val DROPBOX_TOKEN_PREF = "access_token"
        val DROPBOX_LOGOUT_PREF = "dropbox_logout"
        val DROPBOX_EMAIL_PREF = "dropbox_email"
        val CONTENT_TEXT_SIZE_PREF = "content_text_size"
    }
}