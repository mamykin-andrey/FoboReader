package ru.mamykin.core.mvvm

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

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + parentJob

    var router: Router? = null // TODO: нормальный механизм навигации

    protected var state: ViewState by Delegates.observable(initialState) { _, _, new ->
        _stateLiveData.value = new
    }
    private val _stateLiveData = MutableLiveData<ViewState>()

    val stateLiveData: LiveData<ViewState>
        get() = _stateLiveData

    abstract fun reduceState(action: Action): ViewState

    open fun onAction(action: Action) {
        state = reduceState(action)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}