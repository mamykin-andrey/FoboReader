package ru.mamykin.foboreader.core.platform

import android.util.Log
import ru.mamykin.foboreader.BuildConfig

object Log {

    fun error(message: String, tag: String? = "") {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }
}