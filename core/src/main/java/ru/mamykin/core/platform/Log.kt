package ru.mamykin.core.platform

import android.util.Log
import ru.mamykin.core.BuildConfig

object Log {

    private const val DEFAULT_TAG = "foboreader"

    fun error(message: String, tag: String? = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }

    fun debug(message: String, tag: String? = DEFAULT_TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }
}