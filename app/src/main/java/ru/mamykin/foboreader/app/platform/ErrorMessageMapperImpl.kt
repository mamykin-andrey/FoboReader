package ru.mamykin.foboreader.app.platform

import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.presentation.StringOrResource
import java.io.IOException
import javax.inject.Inject

internal class ErrorMessageMapperImpl @Inject constructor() : ErrorMessageMapper {

    override fun getMessage(th: Throwable): StringOrResource {
        val throwableMessage = th.message
        return when {
            th is IOException -> StringOrResource.Resource(R.string.network_error_message)
            !throwableMessage.isNullOrBlank() -> StringOrResource.String(throwableMessage)
            else -> StringOrResource.Resource(R.string.common_error_message)
        }
    }
}