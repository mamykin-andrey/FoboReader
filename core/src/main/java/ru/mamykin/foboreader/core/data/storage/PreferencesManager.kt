package ru.mamykin.foboreader.core.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesManager constructor(
    context: Context
) {
    private val sharedPreferences: SharedPreferences = context
        .applicationContext
        .getSharedPreferences("foboreader", Context.MODE_PRIVATE)

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defValue)

    fun putInt(key: String, value: Int) {
        sharedPreferences.edit {
            putInt(key, value)
        }
    }

    fun getInt(key: String): Int? = sharedPreferences.getInt(key, -1)
        .takeIf { it != -1 }
}