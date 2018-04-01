package ru.mamykin.foboreader.data

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(context: Context) {

    companion object {
        private const val PREFERENCES_FILE = "foboreader"
    }

    private val sharedPreferences: SharedPreferences = context
            .getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)

    fun putBoolean(key: String, value: Boolean) {
        val sharedPreferences = sharedPreferences
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.getBoolean(key, defValue)
    }

    fun getBoolean(key: String): Boolean {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.getBoolean(key, false)
    }

    fun putString(key: String, value: String?) {
        val sharedPreferences = sharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    fun getString(key: String, defValue: String?): String? {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.getString(key, defValue)
    }

    fun getString(key: String): String? {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.getString(key, null)
    }

    fun putInt(key: String, value: Int) {
        val sharedPreferences = sharedPreferences
        val editor = sharedPreferences.edit()
        editor.putInt(key, value).apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.getInt(key, defValue)
    }

    fun getInt(key: String): Int {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.getInt(key, -1)
    }

    fun putFloat(key: String, value: Float) {
        val sharedPreferences = sharedPreferences
        val editor = sharedPreferences.edit()
        editor.putFloat(key, value).apply()
    }

    fun getFloat(key: String, defValue: Float): Float {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.getFloat(key, defValue)
    }

    fun getFloat(key: String): Float {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.getFloat(key, -1f)
    }

    fun removeValue(key: String) {
        val sharedPreferences = sharedPreferences
        val editor = sharedPreferences.edit()
        editor.remove(key).apply()
    }

    operator fun contains(prefName: String): Boolean {
        val sharedPreferences = sharedPreferences
        return sharedPreferences.contains(prefName)
    }
}