package ru.mamykin.foboreader.core.presentation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import ru.mamykin.foboreader.core.platform.Log

class LoggingEffectChannel<T>(
    capacity: Int = Channel.RENDEZVOUS,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND,
) {
    private val channel = Channel<T>(capacity, onBufferOverflow, onUndeliveredElement = {
        Log.error("Couldn't deliver the effect: $it!")
    })

    suspend fun send(element: T) {
        Log.debug("Effect: $element")
        channel.send(element)
    }

    fun receiveAsFlow(): Flow<T> {
        return channel.receiveAsFlow()
    }
}