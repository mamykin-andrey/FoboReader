package ru.mamykin.core.platform

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class PermissionsManager(
        private val context: Context
) {
    fun hasReadExternalStoragePermission(): Boolean {
        return hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    private fun hasPermissions(vararg permissions: String) = permissions.all(this::isPermissionGranted)

    private fun isPermissionGranted(permission: String) =
            ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}