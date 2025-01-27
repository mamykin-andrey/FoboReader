package ru.mamykin.foboreader.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.platform.Log

abstract class BaseViewModel<I> : ViewModel() {

    fun sendIntent(intent: I) {
        Log.debug("Intent: $intent")
        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    abstract suspend fun handleIntent(intent: I)
}