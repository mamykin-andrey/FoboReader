package ru.mamykin.core.platform

import android.util.Log
import ru.mamykin.core.BuildConfig

object Log {

    fun error(message: String, tag: String? = "foboreader") {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }
}