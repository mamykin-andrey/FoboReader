package ru.mamykin.foboreader.core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

abstract class FlowUseCase<in P, out R> {

    operator fun invoke(): Flow<Result<R>> {
        return execute()
            .map { Result.success(it) }
            .catch { emit(Result.error(it)) }
    }

    protected abstract fun execute(): Flow<R>
}