package ru.mamykin.foboreader.core.domain.usecase.base

sealed class Result<out T> {

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> error(exception: Throwable): Result<T> = Error(exception)
    }

    data class Success<T>(val data: T) : Result<T>()
    class Error<T>(val exception: Throwable) : Result<T>()

    fun getOrThrow(): T {
        return when (this) {
            is Success -> data
            is Error -> throw exception
        }
    }

    fun doOnSuccess(onSuccess: (T) -> Unit): Result<T> {
        val data = (this as? Success)?.data ?: return this
        onSuccess(data)
        return this
    }

    fun doOnError(onError: (Throwable) -> Unit): Result<T> {
        val data = (this as? Error)?.exception ?: return this
        onError(data)
        return this
    }

    inline fun <M> catchMap(onSuccess: (T) -> M, onError: (Throwable) -> M): M {
        return when (this) {
            is Success<T> -> onSuccess(data)
            is Error -> onError(exception)
        }
    }
}