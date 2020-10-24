package ru.mamykin.foboreader.core.extension

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

fun Fragment.doOnDestroyView(block: () -> Unit) {
    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroyView() {
            block()
            viewLifecycleOwner.lifecycle.removeObserver(this)
        }
    })
}