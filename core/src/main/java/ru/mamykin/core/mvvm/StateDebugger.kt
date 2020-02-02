package ru.mamykin.core.mvvm

import ru.mamykin.core.platform.Log

class StateDebugger<State> {

    fun onStateChanged(state: State) {
        Log.debug(state.toString(), tag = "ViewState")
    }
}