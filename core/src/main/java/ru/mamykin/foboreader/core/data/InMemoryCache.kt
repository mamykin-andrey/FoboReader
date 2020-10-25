package ru.mamykin.foboreader.core.data

class InMemoryCache<T> {

    private var data: T? = null

    fun get(): T? = data

    suspend fun getOrFetch(newData: suspend () -> T): T {
        return data ?: newData()
            .also { set(it) }
    }

    fun set(data: T) {
        this.data = data
    }
}