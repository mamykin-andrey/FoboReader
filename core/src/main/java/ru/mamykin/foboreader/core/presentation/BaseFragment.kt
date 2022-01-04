package ru.mamykin.foboreader.core.presentation

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import ru.mamykin.foboreader.core.di.ComponentHolder

abstract class BaseFragment(
    @LayoutRes layoutId: Int
) : Fragment(layoutId) {

    abstract val featureName: String

    override fun onDestroy() {
        super.onDestroy()
        if (requireActivity().isFinishing) {
            ComponentHolder.clearComponent(featureName)
            onCleared()
        }
    }

    abstract fun onCleared()
}