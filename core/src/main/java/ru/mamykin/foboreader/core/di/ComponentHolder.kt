package ru.mamykin.foboreader.core.di

object ComponentHolder {

    private var componentMap: MutableMap<String, Any?> = hashMapOf()

    @Suppress("unchecked_cast")
    fun <T> getOrCreateComponent(key: String, createComponent: () -> T): T {
        val component = componentMap[key] ?: createComponent().also { componentMap[key] = it }
        return component as T
    }

    fun clearComponent(key: String) {
        componentMap[key] = null
    }
}