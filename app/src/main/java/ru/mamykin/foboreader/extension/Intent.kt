package ru.mamykin.foboreader.extension

import android.content.Intent

fun Intent.getNullableStringExtra(key: String): String? {
    if (!this.extras.containsKey(key)) {
        return null
    }
    return this.extras.getString(key)
}

fun Intent.getNullableIntExtra(key: String): Int? {
    if (!this.extras.containsKey(key)) {
        return null
    }
    return this.extras.getInt(key)
}