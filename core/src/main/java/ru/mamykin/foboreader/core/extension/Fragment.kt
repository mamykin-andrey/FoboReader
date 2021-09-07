package ru.mamykin.foboreader.core.extension

import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider

//fun Fragment.doOnDestroyView(block: () -> Unit) {
//    viewLifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
//        @Suppress("unused")
//        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
//        fun onDestroyView() {
//            block()
//            viewLifecycleOwner.lifecycle.removeObserver(this)
//        }
//    })
//}

fun Fragment.apiHolder(): ApiHolder {
    return (requireActivity().application as ApiHolderProvider).apiHolder
}