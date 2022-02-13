package ru.mamykin.foboreader.core.di.api

import android.content.Context
import ru.mamykin.foboreader.core.platform.ErrorMessageMapper
import ru.mamykin.foboreader.core.platform.NotificationManager
import ru.mamykin.foboreader.core.platform.ResourceManager
import ru.mamykin.foboreader.core.platform.VibratorHelper

interface CommonApi {

    fun context(): Context

    fun resourceManager(): ResourceManager

    fun notificationManager(): NotificationManager

    fun vibratorHelper(): VibratorHelper

    fun errorMessageMapper(): ErrorMessageMapper
}