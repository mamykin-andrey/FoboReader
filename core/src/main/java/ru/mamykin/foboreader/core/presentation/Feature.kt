package ru.mamykin.foboreader.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.mamykin.foboreader.core.platform.Log

typealias ReducerResult<State, Effect> = Pair<State, Set<Effect>>

typealias Actor<Intent, Action> = (intent: Intent) -> Flow<Action>

typealias Reducer<State, Action, Effect> = (state: State, action: Action) -> ReducerResult<State, Effect>

abstract class Feature<State, Intent, Effect, Action>(
    initialState: State,
    private val actor: Actor<Intent, Action>,
    private val reducer: Reducer<State, Action, Effect>
) : ViewModel() {

    companion object {
        private const val TAG = "Feature"
    }

    private val _stateData = MutableLiveData(initialState)
    val stateData: LiveData<State> get() = _stateData

    private val _effectData = SingleLiveEvent<Effect>()
    val effectData: LiveData<Effect> get() = _effectData

    protected val state: State
        get() = _stateData.value!!

    protected fun sendIntent(intent: Intent) {
        Log.debug("intent: $intent", TAG)
        actor.invoke(intent).onEach {
            Log.debug("action: $it", TAG)
            val (state, effects) = reducer.invoke(state, it)

            Log.debug("state: $state", TAG)
            _stateData.value = state

            Log.debug("effects: $effects", TAG)
            effects.forEach(_effectData::setValue)
        }.launchIn(viewModelScope)
    }
}