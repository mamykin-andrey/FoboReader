package ru.mamykin.foboreader.core.domain.usecase.base

abstract class SuspendUseCase<in P, out R> {

    suspend operator fun invoke(param: P): Result<R> {
        return runCatching {
            Result.success(execute(param))
        }.getOrElse { Result.error(it) }
    }

    protected abstract suspend fun execute(param: P): R
}