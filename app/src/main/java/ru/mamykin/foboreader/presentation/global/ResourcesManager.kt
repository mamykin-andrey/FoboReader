package ru.mamykin.foboreader.presentation.global

import android.content.Context
import android.support.annotation.StringRes
import javax.inject.Inject

class ResourcesManager @Inject constructor(
        private val context: Context
) {
    fun getString(@StringRes resourceId: Int): String = context.getString(resourceId)
}