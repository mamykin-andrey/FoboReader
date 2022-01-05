package ru.mamykin.foboreader.core.di.api

import android.content.Context
import ru.mamykin.foboreader.core.platform.ResourceManager
import ru.mamykin.foboreader.core.platform.VibratorHelper

interface CommonApi {

    fun context(): Context

    fun resourceManager(): ResourceManager

    fun vibratorHelper(): VibratorHelper
}