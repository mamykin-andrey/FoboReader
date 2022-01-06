package ru.mamykin.foboreader.app.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.mamykin.foboreader.core.data.storage.PreferencesManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PreferencesManagerImpl @Inject constructor(
    context: Context,
) : PreferencesManager {

    private val sharedPreferences: SharedPreferences = context
        .applicationContext
        .getSharedPreferences("foboreader", Context.MODE_PRIVATE)

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit {
            putBoolean(key, value)
        }
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean =
        sharedPreferences.getBoolean(key, defValue)

    override fun putInt(key: String, value: Int) {
        sharedPreferences.edit {
            putInt(key, value)
        }
    }

    override fun getInt(key: String): Int? = sharedPreferences.getInt(key, -1)
        .takeIf { it != -1 }

    override fun putString(key: String, value: String?) {
        sharedPreferences.edit {
            putString(key, value)
        }
    }

    override fun getString(key: String): String? = sharedPreferences.getString(key, null)

    override fun observeBooleanChanges(observableKey: String): Flow<Boolean> = callbackFlow {
        val listener: SharedPreferences.OnSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == observableKey) {
                    trySend(sharedPreferences.getBoolean(key, false))
                }
            }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    override fun observeStringChanges(observableKey: String): Flow<String> = callbackFlow {
        val listener: SharedPreferences.OnSharedPreferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == observableKey) {
                    trySend(requireNotNull(sharedPreferences.getString(key, "")))
                }
            }
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }
}