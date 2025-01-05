package ru.mamykin.foboreader.core.extension

import kotlinx.coroutines.CancellationException

inline fun <R> runCatchingCancellable(block: () -> R): Result<R> {
    return runCatching { block() }.onFailure {
        if (it is CancellationException) throw it
    }
}