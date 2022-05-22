package ru.mamykin.foboreader.core.platform

interface ErrorMessageMapper {

    fun getMessage(th: Throwable): String
}