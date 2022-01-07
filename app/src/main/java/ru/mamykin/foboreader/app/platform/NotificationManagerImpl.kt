package ru.mamykin.foboreader.app.platform

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ru.mamykin.foboreader.core.platform.NotificationManager
import javax.inject.Inject

internal class NotificationManagerImpl @Inject constructor(
    private val context: Context,
) : NotificationManager {

    private val notificationManager by lazy { NotificationManagerCompat.from(context) }

    override fun notify(
        @DrawableRes iconRes: Int,
        title: String,
        text: String,
        channelId: String,
        notificationId: Int,
    ) {
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(iconRes)
            .setContentTitle(title)
            .setContentText(text)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}