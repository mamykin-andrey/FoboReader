package ru.mamykin.foboreader.presentation.global

import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import javax.inject.Inject

class PermissionsManager @Inject constructor(
        private val context: Context
) {
    fun hasPermissions(vararg permissions: String) =
            permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED }
}