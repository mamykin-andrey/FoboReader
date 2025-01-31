package ru.mamykin.foboreader.core.extension

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

/**
 * Similar to [runCatching] but with a special handling of [CancellationException].
 */
inline fun <R> runCatchingCancellable(block: () -> R): Result<R> {
    return runCatching { block() }.onFailure {
        if (it is CancellationException) throw it
    }
}

/**
 * Similar to [Result.fold] but with a special handling of [CancellationException].
 */
inline fun <R, T> Result<T>.foldCancellable(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R
): R {
    return fold(
        onSuccess = onSuccess,
        onFailure = {
            if (it is CancellationException)
                throw it
            else
                onFailure(it)
        }
    )
}

/**
 * Launches a coroutine with launch in the [coroutineScope] and returns its [Job].
 */
suspend fun <R> launchInCoroutineScope(block: suspend CoroutineScope.() -> R): Job {
    return coroutineScope {
        launch {
            block()
        }
    }
}

/**
 * Launches a coroutine with launch in the [coroutineScope] and returns its [Job].
 */
suspend fun <R> launchInSupervisorScope(block: suspend CoroutineScope.() -> R): Job {
    return supervisorScope {
        launch {
            block()
        }
    }
}