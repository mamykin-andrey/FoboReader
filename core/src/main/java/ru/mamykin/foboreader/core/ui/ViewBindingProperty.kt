package ru.mamykin.foboreader.core.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun <T> viewBinding(newBinding: () -> T) = ViewBindingProperty(newBinding)

class ViewBindingProperty<T>(
    val newBinding: () -> T
) : ReadOnlyProperty<Fragment, T>, LifecycleObserver {

    private var binding: T? = null

    @Suppress("unused")
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        binding = null
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return binding ?: run {
            newBinding()
                .also { binding = it }
                .also { thisRef.lifecycle.addObserver(this) }
        }
    }
}