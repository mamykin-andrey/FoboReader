package ru.mamykin.foboreader.core.platform

import androidx.annotation.StringRes

interface ResourceManager {

    fun getString(@StringRes stringRes: Int): String
}