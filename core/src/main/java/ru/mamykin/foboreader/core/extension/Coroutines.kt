package ru.mamykin.foboreader.core.extension

import kotlinx.coroutines.CancellationException

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