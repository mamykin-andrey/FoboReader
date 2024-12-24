package ru.mamykin.foboreader.core.platform

import androidx.annotation.DrawableRes

object NotificationChannelId {
    const val GENERAL = "general"
}

interface NotificationManager {

    fun notify(
        @DrawableRes iconRes: Int,
        title: String,
        text: String,
        channelId: String,
        notificationId: Int,
    )

    fun initNotificationChannels()
}