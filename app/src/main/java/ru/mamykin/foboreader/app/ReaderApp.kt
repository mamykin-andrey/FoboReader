package ru.mamykin.foboreader.app

import androidx.multidex.MultiDexApplication
import leakcanary.LeakCanary
import ru.mamykin.foboreader.BuildConfig
import ru.mamykin.foboreader.app.di.ApiHolderImpl
import ru.mamykin.foboreader.app.di.DaggerAppComponent
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.core.platform.NotificationManager
import javax.inject.Inject

@Suppress("unused")
class ReaderApp : MultiDexApplication(), ApiHolderProvider {

    override val apiHolder = ApiHolderImpl(this)

    @Inject
    internal lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()
        initDi()
        initLeakCanary(enabled = BuildConfig.DEBUG)
        initLogger()
        notificationManager.initNotificationChannels()
    }

    private fun initLeakCanary(enabled: Boolean) {
        LeakCanary.config = LeakCanary.config.copy(
            dumpHeap = enabled,
            retainedVisibleThreshold = 2
        )
    }

    private fun initLogger() {
        Log.init(BuildConfig.DEBUG)
    }

    private fun initDi() {
        DaggerAppComponent.factory().create(this).inject(this)
    }
}