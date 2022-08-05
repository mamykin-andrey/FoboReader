package ru.mamykin.foboreader.app.platform

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.ResourceManager
import java.io.IOException

internal class ErrorMessageMapperImplTest {

    private val networkErrorMessage = "network_error"
    private val commonErrorMessage = "common_error"
    private val throwableMessage = "throwable_error"
    private val resourceManager: ResourceManager = mockk()
    private val errorMessageMapper: ErrorMessageMapper = ErrorMessageMapperImpl(resourceManager)

    @Before
    fun setUp() {
        every { resourceManager.getString(R.string.network_error_message) } returns networkErrorMessage
        every { resourceManager.getString(R.string.common_error_message) } returns commonErrorMessage
    }

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