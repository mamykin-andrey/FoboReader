package ru.mamykin.foboreader.data.storage

import android.content.Context
import android.content.SharedPreferences
import ru.mamykin.foboreader.ReaderApp
import javax.inject.Inject

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
class PreferencesManager : PreferenceNames {
    @Inject
    lateinit var context: Context

    private val sharedPreferences: SharedPreferences
        get() = context!!.getSharedPreferences(PreferenceNames.PREFERENCES_FILE, Context.MODE_PRIVATE)

    init {
        ReaderApp.component.inject(this)
    }

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

    fun putString(key: String, value: String) {
        val sharedPreferences = sharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    fun getString(key: String, defValue: String): String? {
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