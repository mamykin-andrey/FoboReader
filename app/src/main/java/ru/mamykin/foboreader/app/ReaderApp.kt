package ru.mamykin.foboreader.app

import androidx.multidex.MultiDexApplication
import com.github.terrakok.cicerone.Cicerone
import leakcanary.LeakCanary
import ru.mamykin.foboreader.app.di.ApiHolderImpl
import ru.mamykin.foboreader.app.di.AppComponent
import ru.mamykin.foboreader.app.di.DaggerAppComponent
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider
import ru.mamykin.foboreader.core.platform.NotificationUtils

@Suppress("unused")
class ReaderApp : MultiDexApplication(), ApiHolderProvider {

    private lateinit var appComponent: AppComponent
    private val cicerone = Cicerone.create()

    override val apiHolder = ApiHolderImpl(this, cicerone)

    override fun onCreate() {
        super.onCreate()
        initDi()
        NotificationUtils.initNotificationChannels(this)
        initLeakCanary()
    }

    private fun initDi() {
        appComponent = DaggerAppComponent.factory().create(this, cicerone)
    }

    private fun initLeakCanary() {
        val newConfig = LeakCanary.config.copy(
            retainedVisibleThreshold = 2
        )
        LeakCanary.config = newConfig
    }
}