package ru.mamykin.foboreader.core.platform

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast

object OsHelper {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    fun isM(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }
}