package ru.mamykin.foboreader.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

typealias ReducerResult<State, Effect> = Pair<State, Set<Effect>>

typealias Actor<Intent, Action> = (intent: Intent) -> Flow<Action>

typealias Reducer<State, Action, Effect> = (state: State, action: Action) -> ReducerResult<State, Effect>

abstract class Feature<State, Intent, Effect, Action, SideEffect>(
    initialState: State,
    private val actor: Actor<Intent, Action>,
    private val reducer: Reducer<State, Action, Effect>
) : ViewModel() {

    private val _stateData = MutableLiveData(initialState)
    val stateData: LiveData<State> get() = _stateData

    private val _effectData = SingleLiveEvent<Effect>()
    val effectData: LiveData<Effect> get() = _effectData

    protected val state: State
        get() = _stateData.value!!

    protected fun sendIntent(intent: Intent) {
        actor.invoke(intent).onEach {
            val (state, effects) = reducer.invoke(state, it)
            _stateData.value = state
            effects.forEach(_effectData::setValue)
        }.launchIn(viewModelScope)
    }
}