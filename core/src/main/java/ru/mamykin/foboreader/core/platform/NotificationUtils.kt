package ru.mamykin.foboreader.core.platform

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build

object NotificationUtils {

    const val GENERAL_CHANNEL_ID = "general"
    const val FILE_DOWNLOAD_NOTIFICATION_ID = 1

    fun initNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        initGeneralChannel(context)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun initGeneralChannel(context: Context) {
        val notificationManager = context.getSystemService(
                Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        val channel = NotificationChannel(
                GENERAL_CHANNEL_ID,
                "Общие уведомления",
                NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Общие уведомления от приложения"
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
}