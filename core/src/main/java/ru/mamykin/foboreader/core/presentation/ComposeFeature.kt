package ru.mamykin.foboreader.core.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import ru.mamykin.foboreader.core.platform.Log

typealias ReducerResult<State, Effect> = Pair<State, Set<Effect>>
typealias Actor<Intent, Action> = (intent: Intent) -> Flow<Action>
typealias Reducer<State, Action, Effect> = (state: State, action: Action) -> ReducerResult<State, Effect>

@Deprecated("Use Jetpack ViewModel instead")
abstract class ComposeFeature<State, Intent, Effect, Action>(
    initialState: State,
    private val actor: Actor<Intent, Action>,
    private val reducer: Reducer<State, Action, Effect>,
    private val scope: CoroutineScope,
) {
    companion object {
        private const val TAG = "ComposeFeature"
    }

    var state: State by mutableStateOf(initialState)

    private val effectChannel = Channel<Effect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

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