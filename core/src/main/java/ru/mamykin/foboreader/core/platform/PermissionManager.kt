package ru.mamykin.foboreader.core.platform

import androidx.appcompat.app.AppCompatActivity

interface PermissionManager {

    fun init(activity: AppCompatActivity)

    suspend fun requestPermissions(
        vararg permissions: RequestedPermission,
    ): Boolean
}

enum class RequestedPermission {
    NOTIFICATIONS;
}