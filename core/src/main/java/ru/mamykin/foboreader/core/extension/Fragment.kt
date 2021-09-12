package ru.mamykin.foboreader.core.extension

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
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

/**
 * Add a back pressed dispatcher to activity, using viewLifecycleOwner
 */
fun Fragment.addBackPressedDispatcher(backPressed: () -> Unit) {
    lifecycleScope.launchWhenStarted {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    backPressed()
                }
            }
        )
    }
}