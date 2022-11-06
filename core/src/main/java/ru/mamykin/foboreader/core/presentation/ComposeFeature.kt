package ru.mamykin.foboreader.core.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import ru.mamykin.foboreader.core.platform.Log

abstract class ComposeFeature<State, Intent, Effect, Action>(
    initialState: State,
    private val actor: Actor<Intent, Action>,
    private val reducer: Reducer<State, Action, Effect>
) {
    companion object {
        private const val TAG = "ComposeFeature"
    }

    var state: State by mutableStateOf(initialState)

    private val effectChannel = Channel<Effect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun sendIntent(intent: Intent) {
        Log.debug("intent: $intent", TAG)
        actor.invoke(intent).onEach {
            Log.debug("action: $it", TAG)
            val (state, effects) = reducer.invoke(state, it)

            Log.debug("state: $state", TAG)
            this.state = state

            Log.debug("effects: $effects", TAG)
            effects.forEach(effectChannel::trySend)
        }.launchIn(scope)
    }

    fun onCleared() {
        scope.cancel()
    }
}