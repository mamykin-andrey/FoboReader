package ru.mamykin.foboreader.app

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import ru.mamykin.foboreader.BuildConfig
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.core.platform.NotificationManager
import javax.inject.Inject

@Suppress("unused")
@HiltAndroidApp
class ReaderApp : MultiDexApplication() {

    @Inject
    internal lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        initLogger()
        notificationManager.initNotificationChannels()
    }

    private fun initLogger() {
        Log.init(BuildConfig.DEBUG)
    }
}