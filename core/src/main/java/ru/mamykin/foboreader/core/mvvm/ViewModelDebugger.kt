package ru.mamykin.foboreader.core.mvvm

import ru.mamykin.foboreader.core.platform.Log

class ViewModelDebugger<Event, State, Effect> {

    companion object {
        private const val TAG_VIEWMODEL = "ViewModel"
    }

    fun onEvent(action: Event) {
        Log.debug("event: " + action.toString(), tag = TAG_VIEWMODEL)
    }

    fun onSetState(state: State) {
        Log.debug("state: " + state.toString(), tag = TAG_VIEWMODEL)
    }

    fun onEffect(effect: Effect) {
        Log.debug("effect: " + effect.toString(), tag = TAG_VIEWMODEL)
    }
}