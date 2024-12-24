package ru.mamykin.foboreader.core.platform

import androidx.appcompat.app.AppCompatActivity

interface PermissionManager {

    fun checkPermission(
        permission: RequestedPermission,
    ): Boolean

    suspend fun requestPermission(
        activity: AppCompatActivity,
        permission: RequestedPermission,
    ): Boolean
}

enum class RequestedPermission {
    NOTIFICATIONS;
}