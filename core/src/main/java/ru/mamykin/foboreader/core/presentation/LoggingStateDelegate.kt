package ru.mamykin.foboreader.core.presentation

import androidx.compose.runtime.mutableStateOf
import ru.mamykin.foboreader.core.platform.Log
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class LoggingStateDelegate<T>(
    initialValue: T
) : ReadWriteProperty<Any?, T> {

    private val state = mutableStateOf(initialValue)

    override fun getValue(thisRef: Any?, property: KProperty<*>): T = state.value

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        Log.debug("State: $value")
        state.value = value
    }
}