package ru.mamykin.foboreader.ui.devicebooks

import android.app.Activity
import ru.mamykin.foboreader.ui.readbook.ReadBookActivity

class DeviceBooksRouter(
        private val activity: Activity
) {
    fun openBook(path: String) {
        activity.startActivity(ReadBookActivity.getStartIntent(activity, path))
    }
}