package ru.mamykin.foboreader.core.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

abstract class BaseViewModel<ViewState, Action, Event, Effect>(
    initialState: ViewState
) : ViewModel() {

    private val stateDebugger = ViewModelDebugger<Event, ViewState, Effect>()

    protected var state: ViewState by Delegates.observable(initialState) { _, _, new ->
        _stateLiveData.value = new
        stateDebugger.onSetState(new)
    }
    private val _stateLiveData = MutableLiveData<ViewState>()
    val stateLiveData: LiveData<ViewState> get() = _stateLiveData

    private val _effectLiveData = SingleLiveEvent<Effect>()
    val effectLiveData: LiveData<Effect> get() = _effectLiveData

    init {
        state = initialState
    }

    protected fun sendAction(action: Action) {
        state = onAction(action)
    }

    abstract fun onAction(action: Action): ViewState

    fun sendEvent(event: Event) {
        onEvent(event)
        stateDebugger.onEvent(event)
    }

    protected open fun onEvent(event: Event) {}

    protected fun sendEffect(effect: Effect) {
        _effectLiveData.setValue(effect)
        stateDebugger.onEffect(effect)
    }
}