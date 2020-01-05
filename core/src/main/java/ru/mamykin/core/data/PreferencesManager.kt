package ru.mamykin.core.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager constructor(
        context: Context
) {
    private val sharedPreferences: SharedPreferences = context
            .applicationContext
            .getSharedPreferences("foboreader", Context.MODE_PRIVATE)

    fun remove(key: String) {
        sharedPreferences
                .edit()
                .remove(key)
                .apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences
                .edit()
                .putBoolean(key, value)
                .apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean = sharedPreferences.getBoolean(key, defValue)

    fun putString(key: String, value: String?) {
        sharedPreferences
                .edit()
                .putString(key, value)
                .apply()
    }

    fun getString(key: String): String? = sharedPreferences.getString(key, null)

    fun putInt(key: String, value: Int) {
        sharedPreferences
                .edit()
                .putInt(key, value)
                .apply()
    }

    @Deprecated("Deprecated. Use fun getInt(key: String) instead")
    fun getInt(key: String, defValue: Int): Int = sharedPreferences.getInt(key, defValue)

    fun getInt(key: String): Int? = sharedPreferences.getInt(key, -1)
            .takeIf { it != -1 }

    fun putFloat(key: String, value: Float) {
        sharedPreferences
                .edit()
                .putFloat(key, value)
                .apply()
    }

    fun getFloat(key: String): Float? = sharedPreferences.getFloat(key, -1f).takeIf { it != -1f }
}