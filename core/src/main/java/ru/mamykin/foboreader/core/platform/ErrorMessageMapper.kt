package ru.mamykin.foboreader.core.platform

import ru.mamykin.foboreader.core.presentation.StringOrResource

interface ErrorMessageMapper {

    fun getMessage(th: Throwable): StringOrResource
}