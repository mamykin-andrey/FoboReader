package ru.mamykin.foboreader.core.data.storage

import kotlinx.coroutines.flow.Flow

interface PreferencesManager {

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defValue: Boolean): Boolean
    fun putInt(key: String, value: Int)
    fun getInt(key: String): Int?
    fun putString(key: String, value: String?)
    fun getString(key: String): String?
    fun observeBooleanChanges(observableKey: String): Flow<Boolean>
    fun observeStringChanges(observableKey: String): Flow<String>
}