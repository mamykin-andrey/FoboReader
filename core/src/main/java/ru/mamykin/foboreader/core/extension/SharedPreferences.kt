package ru.mamykin.foboreader.core.extension

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

inline fun <reified T> SharedPreferences.putValue(key: String, value: T) {
    val editor = edit()
    when (T::class) {
        String::class -> editor.putString(key, value as String)
        Boolean::class -> editor.putBoolean(key, value as Boolean)
        Int::class -> editor.putInt(key, value as Int)
        else -> throw IllegalArgumentException("Unsupported type: ${T::class}!")
    }
    editor.apply()
}

inline fun <reified T> SharedPreferences.getValue(key: String, default: T): T {
    return when (T::class) {
        String::class -> getString(key, default as String) as T
        Boolean::class -> getBoolean(key, default as Boolean) as T
        Int::class -> getInt(key, default as Int) as T
        else -> throw IllegalArgumentException("Unsupported type: ${T::class}!")
    }
}

inline fun <reified T> SharedPreferences.observeChanges(key: String): Flow<T> = callbackFlow {
    val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, prefKey ->
            if (prefKey == key) {
                offer(prefs.getValue(key, "") as T)
            }
        }
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}