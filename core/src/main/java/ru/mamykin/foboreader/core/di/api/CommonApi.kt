package ru.mamykin.foboreader.core.di.api

import android.content.Context
import ru.mamykin.foboreader.core.platform.VibratorHelper

interface CommonApi {

    fun context(): Context

    fun vibratorHelper(): VibratorHelper
}