package ru.mamykin.foboreader.app.platform

import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.ResourceManager
import java.io.IOException
import javax.inject.Inject

internal class ErrorMessageMapperImpl @Inject constructor(
    private val resourceManager: ResourceManager,
) : ErrorMessageMapper {

    override fun getMessage(th: Throwable): String? = when (th) {
        is IOException -> resourceManager.getString(R.string.network_error_message)
        else -> th.message
    }
}