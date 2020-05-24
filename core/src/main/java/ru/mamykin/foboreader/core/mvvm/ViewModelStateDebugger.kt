package ru.mamykin.foboreader.core.mvvm

import ru.mamykin.foboreader.core.platform.Log

class ViewModelStateDebugger<Event, State> {

    companion object {
        private const val TAG_STATE = "ViewModelState"
    }

    fun onEvent(action: Event) {
        Log.debug("event: " + action.toString(), tag = TAG_STATE)
    }

    fun onStateChanged(state: State) {
        Log.debug("state: " + state.toString(), tag = TAG_STATE)
    }
}