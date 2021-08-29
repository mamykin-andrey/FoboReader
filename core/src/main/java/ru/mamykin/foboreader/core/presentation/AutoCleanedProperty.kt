package ru.mamykin.foboreader.core.presentation

import android.os.Handler
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> Fragment.autoCleanedValue(initializer: () -> T) = AutoCleanedProperty(initializer)

class AutoCleanedProperty<T>(
    val initializer: () -> T
) : ReadOnlyProperty<Fragment, T>, LifecycleObserver {

    private var value: T? = null
    private var lifecycle: Lifecycle? = null

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        lifecycle?.removeObserver(this)
        Handler().post {
            lifecycle = null
            value = null
        }
    }

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return value ?: run {
            initializer()
                .also { value = it }
                .also { lifecycle = thisRef.viewLifecycleOwner.lifecycle }
                .also { lifecycle?.addObserver(this) }
        }
    }
}