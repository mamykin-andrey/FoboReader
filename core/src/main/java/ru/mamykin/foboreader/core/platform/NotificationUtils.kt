package ru.mamykin.foboreader.core.platform

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.content.getSystemService
import ru.mamykin.foboreader.core.R

object ChannelId {

    const val GENERAL = "general"
}

object NotificationId {

    const val FILE_DOWNLOAD = 1
}

object NotificationUtils {

    fun initNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        initGeneralChannel(context)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun initGeneralChannel(context: Context) {
        val notificationManager = context.getSystemService<NotificationManager>()!!

        val channel = NotificationChannel(
            ChannelId.GENERAL,
            context.getString(R.string.core_error_retry_title),
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.core_general_notifications_description)
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
}