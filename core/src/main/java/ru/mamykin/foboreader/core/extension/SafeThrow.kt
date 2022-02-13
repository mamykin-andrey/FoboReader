package ru.mamykin.foboreader.core.extension

import ru.mamykin.foboreader.core.BuildConfig
import ru.mamykin.foboreader.core.platform.Log

private const val TAG_SAFE_THROW = "safe_throw"

fun safeThrow(th: Throwable) {
    if (BuildConfig.DEBUG) {
        throw th
    } else {
        Log.error(th.message.orEmpty(), TAG_SAFE_THROW)
    }
}