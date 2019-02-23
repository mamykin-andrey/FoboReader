package ru.mamykin.foboreader.presentation.dropboxbooks

import android.app.Activity
import com.dropbox.core.android.Auth
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.presentation.readbook.ReadBookActivity

class DropboxBooksRouter(private val activity: Activity) {

    fun startOAuth2Authentication() {
        Auth.startOAuth2Authentication(activity, activity.getString(R.string.dropbox_api_key))
    }

    fun openBook(path: String) {
        activity.startActivity(ReadBookActivity.getStartIntent(activity, path))
    }
}