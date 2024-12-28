package ru.mamykin.foboreader.app.platform

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import java.io.IOException

internal class ErrorMessageMapperImplTest {

    private val networkErrorMessage = "network_error"
    private val commonErrorMessage = "common_error"
    private val throwableMessage = "throwable_error"
    private val errorMessageMapper: ErrorMessageMapper = ErrorMessageMapperImpl()

    @Test
    fun `getMessage returns network error message when exception is IOException`() {
        val ex = IOException("internal_network_error")

        val message = errorMessageMapper.getMessage(ex)

        assertEquals(networkErrorMessage, message)
    }

    @Test
    fun `getMessage returns throwable error when message is not blank`() {
        val ex = IllegalStateException(throwableMessage)

        val message = errorMessageMapper.getMessage(ex)

        assertEquals(throwableMessage, message)
    }

    @Test
    fun `getMessage returns common error when message is null`() {
        val ex = IllegalStateException()

        val message = errorMessageMapper.getMessage(ex)

        assertEquals(commonErrorMessage, message)
    }

    @Test
    fun `getMessage returns common error when message is blank`() {
        val ex = IllegalStateException("")

        val message = errorMessageMapper.getMessage(ex)

        assertEquals(commonErrorMessage, message)
    }
}