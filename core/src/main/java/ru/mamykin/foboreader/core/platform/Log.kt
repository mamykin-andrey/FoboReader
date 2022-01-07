package ru.mamykin.foboreader.core.platform

import android.util.Log

object Log {

    private const val DEFAULT_TAG = "foboreader"
    private var enabled = false

    fun init(enabled: Boolean) {
        this.enabled = enabled
    }

    fun error(message: String, tag: String? = DEFAULT_TAG) {
        if (enabled) {
            Log.e(tag, message)
        }
    }

    fun debug(message: String, tag: String? = DEFAULT_TAG) {
        if (enabled) {
            Log.d(tag, message)
        }
    }
}