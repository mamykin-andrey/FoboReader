package ru.mamykin.foboreader.app.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PreferencesManagerImpl @Inject constructor(
    context: Context,
) : PreferencesManager {

    companion object {
        private const val COMMON_PREFS_NAME = "foboreader"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        COMMON_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    override fun putBoolean(key: String, value: Boolean) = sharedPreferences.edit { putBoolean(key, value) }

    override fun getBoolean(key: String, defValue: Boolean): Boolean = sharedPreferences.getBoolean(key, defValue)

    override fun putInt(key: String, value: Int) = sharedPreferences.edit { putInt(key, value) }

    override fun putString(key: String, value: String?) = sharedPreferences.edit { putString(key, value) }

    override fun getInt(key: String, defValue: Int): Int = sharedPreferences.getInt(key, defValue)

    override fun getString(key: String, defValue: String): String = sharedPreferences.getString(key, defValue)!!
}