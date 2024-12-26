package ru.mamykin.foboreader.app.platform

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.mamykin.foboreader.core.platform.ResourceManager
import javax.inject.Inject

internal class ResourceManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ResourceManager {

    override fun getString(@StringRes stringRes: Int): String {
        return context.getString(stringRes)
    }
}