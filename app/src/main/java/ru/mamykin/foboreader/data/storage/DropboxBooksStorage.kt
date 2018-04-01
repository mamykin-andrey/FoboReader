package ru.mamykin.foboreader.data.storage

import javax.inject.Inject

class DropboxBooksStorage @Inject constructor(
        private val prefManager: PreferencesManager
) {
    companion object {
        const val DROPBOX_TOKEN_PREF = "access_token"
    }

    var authToken: String?
        get() = prefManager.getString(DROPBOX_TOKEN_PREF, null)
        set(value) = prefManager.putString(DROPBOX_TOKEN_PREF, value)
}