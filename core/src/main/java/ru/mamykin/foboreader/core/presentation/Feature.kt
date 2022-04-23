package ru.mamykin.foboreader.core.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import ru.mamykin.foboreader.core.platform.Log

typealias ReducerResult<State, Effect> = Pair<State, Set<Effect>>

typealias Actor<Intent, Action> = (intent: Intent) -> Flow<Action>

typealias Reducer<State, Action, Effect> = (state: State, action: Action) -> ReducerResult<State, Effect>

abstract class Feature<State, Intent, Effect, Action>(
    initialState: State,
    private val actor: Actor<Intent, Action>,
    private val reducer: Reducer<State, Action, Effect>
) {
    companion object {
        private const val TAG = "Feature"
    }

    private val _stateFlow = MutableStateFlow(initialState)
    val stateFlow: Flow<State> get() = _stateFlow

    private val effectChannel = Channel<Effect>(Channel.BUFFERED)
    val effectFlow = effectChannel.receiveAsFlow()

    protected val state: State
        get() = requireNotNull(_stateFlow.value)

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun sendIntent(intent: Intent) {
        Log.debug("intent: $intent", TAG)
        actor.invoke(intent).onEach {
            Log.debug("action: $it", TAG)
            val (state, effects) = reducer.invoke(state, it)

            Log.debug("state: $state", TAG)
            _stateFlow.value = state

            Log.debug("effects: $effects", TAG)
            effects.forEach(effectChannel::trySend)
        }.launchIn(scope)
    }

    fun onCleared() {
        scope.cancel()
    }
}