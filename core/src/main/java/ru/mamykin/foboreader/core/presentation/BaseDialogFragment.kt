package ru.mamykin.foboreader.core.presentation

import androidx.fragment.app.DialogFragment
import ru.mamykin.foboreader.core.di.ComponentHolder

abstract class BaseDialogFragment : DialogFragment() {

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