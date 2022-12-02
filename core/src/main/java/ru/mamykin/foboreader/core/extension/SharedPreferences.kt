package ru.mamykin.foboreader.core.extension

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

inline fun <reified T> SharedPreferences.observeChanges(key: String): Flow<T> = callbackFlow {
    val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { prefs, prefKey ->
            if (prefKey == key) {
                offer(
                    when (T::class) {
                        String::class -> prefs.getString(key, "") as T
                        Boolean::class -> prefs.getBoolean(key, false) as T
                        else -> throw UnsupportedOperationException("Unknown type: ${T::class.simpleName}!")
                    }
                )
            }
        }
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}