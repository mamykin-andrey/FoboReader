package ru.mamykin.foboreader.app.platform

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.CompletableDeferred
import ru.mamykin.foboreader.core.platform.PermissionManager
import ru.mamykin.foboreader.core.platform.RequestedPermission
import javax.inject.Inject

internal class PermissionManagerImpl @Inject constructor(
    private val context: Context
) : PermissionManager {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var requestPermissionDeferred: CompletableDeferred<Boolean>? = null

    override fun init(activity: AppCompatActivity) {
        requestPermissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            requestPermissionDeferred?.complete(isGranted)
        }
    }

    override suspend fun requestPermissions(vararg permissions: RequestedPermission): Boolean {
        return permissions.all(::hasPermission) || permissions.all { requestPermission(it) }
    }

    @SuppressLint("NewApi")
    private fun hasPermission(permission: RequestedPermission): Boolean {
        if (!permission.isRequiredByOs()) return true

        return ActivityCompat.checkSelfPermission(
            context,
            permission.toManifestStr()
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("NewApi")
    private suspend fun requestPermission(permission: RequestedPermission): Boolean {
        requestPermissionDeferred = CompletableDeferred()
        requestPermissionLauncher.launch(permission.toManifestStr())
        return requestPermissionDeferred!!.await()
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