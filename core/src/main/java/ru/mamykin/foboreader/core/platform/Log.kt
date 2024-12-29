package ru.mamykin.foboreader.core.platform

import android.util.Log

object Log {
    private const val DEFAULT_TAG = "foboreader"
    private var isDebug = false

    fun init(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    fun error(message: String, tag: String? = DEFAULT_TAG) {
        if (isDebug) {
            throw IllegalStateException("Unhandled error: $message")
        } else {
            // log non-fatal
        }
    }

    fun warning(message: String, tag: String? = DEFAULT_TAG) {
        if (isDebug) {
            Log.w(tag, message)
        }
    }

    fun debug(message: String, tag: String? = DEFAULT_TAG) {
        if (isDebug) {
            Log.d(tag, message)
        }
    }
}