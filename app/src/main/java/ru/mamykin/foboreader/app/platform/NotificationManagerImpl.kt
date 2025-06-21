package ru.mamykin.foboreader.app.platform

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.mamykin.foboreader.R
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.core.platform.NotificationChannelId
import ru.mamykin.foboreader.core.platform.NotificationManager
import javax.inject.Inject

internal class NotificationManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationManager {

    private val deviceNotificationManager by lazy { NotificationManagerCompat.from(context) }

    override fun initNotificationChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        initGeneralChannel(context)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun initGeneralChannel(context: Context) {
        val notificationManager = context.getSystemService<android.app.NotificationManager>()!!

        val channel = NotificationChannel(
            NotificationChannelId.GENERAL,
            context.getString(R.string.core_general_notifications_title),
            android.app.NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = context.getString(R.string.core_general_notifications_description)
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true)
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun notify(
        @DrawableRes iconRes: Int,
        title: String,
        text: String,
        channelId: String,
        notificationId: Int,
    ) {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.error("No permission for sending notifications!")
            return
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(text)
            .build()

        deviceNotificationManager.notify(notificationId, notification)
    }
}