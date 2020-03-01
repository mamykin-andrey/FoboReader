package ru.mamykin.foboreader.core.mvvm

import ru.mamykin.foboreader.core.platform.Log

class StateDebugger<State> {

    fun onStateChanged(state: State) {
        Log.debug(state.toString(), tag = "ViewState")
    }
}