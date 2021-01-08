package ru.mamykin.foboreader.core.platform

import android.os.Build

object OsHelper {

    fun isQ(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }
}