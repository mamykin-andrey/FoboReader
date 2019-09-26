package ru.mamykin.foboreader.core.platform

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject

class PermissionsManager @Inject constructor(
        private val context: Context
) {
    fun hasPermissions(vararg permissions: String) = permissions.all(this::isPermissionGranted)

    private fun isPermissionGranted(permission: String) =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}