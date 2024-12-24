package ru.mamykin.foboreader.core.data.storage

interface PreferencesManager {

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String, defValue: Boolean): Boolean
    fun putInt(key: String, value: Int)
    fun getInt(key: String, defValue: Int): Int
    fun putString(key: String, value: String?)
    fun getString(key: String, defValue: String): String
}