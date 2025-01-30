package ru.mamykin.foboreader.core.presentation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.core.platform.Log

abstract class BaseViewModel<I, S, E>(
    initialState: S,
) : ViewModel() {

    var uiState = MutableStateFlow(initialState)

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    var state: S = initialState
        set(value) {
            Log.debug("State: $value")
            field = value
            uiState.value = value
        }

    private val effectChannel = Channel<E>(onBufferOverflow = BufferOverflow.SUSPEND, onUndeliveredElement = {
        Log.debug("Couldn't deliver the effect: $it!")
    })
    val effectFlow = effectChannel.receiveAsFlow()

    fun sendIntent(intent: I) {
        Log.debug("Intent: $intent")
        // TODO: Exception handling
        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    protected open suspend fun handleIntent(intent: I) {}

    protected suspend fun sendEffect(effect: E) {
        Log.debug("Effect: $effect")
        effectChannel.send(effect)
    }
}