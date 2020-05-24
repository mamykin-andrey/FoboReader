package ru.mamykin.foboreader.core.mvvm

import androidx.annotation.CallSuper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

abstract class BaseViewModel2<ViewState, Action, Event>(
    initialState: ViewState
) : ViewModel(), CoroutineScope {

    // TODO: check exception in child coroutine
    private val parentJob = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, th -> th.printStackTrace() }
    private val stateDebugger = ViewModelStateDebugger<Event, ViewState>()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob + exceptionHandler

    protected var state: ViewState by Delegates.observable(initialState) { _, _, new ->
        _stateLiveData.value = new
        stateDebugger.onStateChanged(new)
    }
    private val _stateLiveData = MutableLiveData<ViewState>()

    val stateLiveData: LiveData<ViewState>
        get() = _stateLiveData

    init {
        state = initialState
    }

    fun sendAction(action: Action) {
        state = onAction(action)
    }

    abstract fun onAction(action: Action): ViewState

    fun sendEvent(event: Event) {
        launch { onEvent(event) }
        stateDebugger.onEvent(event)
    }

    open suspend fun onEvent(event: Event) {}

    fun loadData() {
        launch { onLoadData() }
    }

    open suspend fun onLoadData() {}

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}