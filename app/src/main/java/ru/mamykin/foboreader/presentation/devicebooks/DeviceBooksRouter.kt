package ru.mamykin.foboreader.presentation.devicebooks

import android.app.Activity
import ru.mamykin.foboreader.presentation.readbook.ReadBookActivity

class DeviceBooksRouter(
        private val activity: Activity
) {
    fun openBook(path: String) {
        ReadBookActivity.start(activity, path)
    }
}