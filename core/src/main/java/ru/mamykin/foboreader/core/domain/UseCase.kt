package ru.mamykin.foboreader.core.domain

abstract class UseCase<in P, out R> {

    operator fun invoke(param: P): Result<R> {
        return runCatching {
            Result.success(execute(param))
        }.getOrElse { Result.error(it) }
    }

    protected abstract fun execute(param: P): R
}