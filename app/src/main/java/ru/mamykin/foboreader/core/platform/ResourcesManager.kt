package ru.mamykin.foboreader.core.platform

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class ResourcesManager @Inject constructor(
        private val context: Context
) {
    fun getString(@StringRes resourceId: Int): String = context.getString(resourceId)
}