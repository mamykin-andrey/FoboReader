package ru.mamykin.foboreader.app.platform

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import ru.mamykin.foboreader.core.platform.PermissionManager
import ru.mamykin.foboreader.core.platform.RequestedPermission
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class PermissionManagerImpl @Inject constructor(
    private val context: Context
) : PermissionManager {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun requestPermission(activity: AppCompatActivity, permission: RequestedPermission): Boolean {
        if (checkPermission(permission)) return true

        var isResultCallback = false
        return suspendCoroutine { cont ->
            activity.registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isResultCallback) {
                    cont.resume(isGranted)
                } else {
                    isResultCallback = true
                }
            }.launch(permission.toManifestStr())
        }
    }

    @SuppressLint("NewApi")
    override fun checkPermission(permission: RequestedPermission): Boolean {
        if (!permission.isRequiredByOs()) return true

        return ActivityCompat.checkSelfPermission(
            context,
            permission.toManifestStr()
        ) == PackageManager.PERMISSION_GRANTED
    }

    @ChecksSdkIntAtLeast
    private fun RequestedPermission.isRequiredByOs(): Boolean = when (this) {
        RequestedPermission.NOTIFICATIONS -> Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun RequestedPermission.toManifestStr(): String = when (this) {
        RequestedPermission.NOTIFICATIONS -> Manifest.permission.POST_NOTIFICATIONS
    }
}