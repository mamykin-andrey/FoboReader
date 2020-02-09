package ru.mamykin.core.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

abstract class BaseViewModel<ViewState, Action, Router>(
        initialState: ViewState
) : ViewModel(), CoroutineScope {

    private val parentJob = Job()
    private val stateDebugger = StateDebugger<ViewState>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    var router: Router? = null // TODO: нормальный механизм навигации

    protected var state: ViewState by Delegates.observable(initialState) { _, _, new ->
        _stateLiveData.value = new
        stateDebugger.onStateChanged(new)
    }
    private val _stateLiveData = MutableLiveData<ViewState>()

    val stateLiveData: LiveData<ViewState>
        get() = _stateLiveData

    abstract fun reduceState(action: Action): ViewState

    init {
        state = initialState
    }

    open fun loadData() {
    }

    open fun onAction(action: Action) {
        state = reduceState(action)
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}