package ru.mamykin.foboreader.core.presentation

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.di.ComponentHolder

// TODO: Remove this class
abstract class BaseFragment : Fragment() {

    // TODO: Automate this field
    abstract val featureName: String

    override fun onDestroy() {
        super.onDestroy()
        if (isRemoving) {
            ComponentHolder.clearComponent(featureName)
            onCleared()
        }
    }

    // TODO: Remove this method and use onDestroy with the flag if needed
    abstract fun onCleared()

    protected fun <T> Flow<T>.collectWithRepeatOnStarted(collector: (T) -> Unit) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect { collector(it) }
            }
        }
    }
}