package ru.mamykin.foboreader.app

import androidx.multidex.MultiDexApplication
import com.github.terrakok.cicerone.Cicerone
import leakcanary.LeakCanary
import ru.mamykin.foboreader.BuildConfig
import ru.mamykin.foboreader.app.di.ApiHolderImpl
import ru.mamykin.foboreader.app.di.CoreCommonComponent
import ru.mamykin.foboreader.app.di.DaggerCoreCommonComponent
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.CommonApiProvider
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.core.platform.NotificationUtils

@Suppress("unused")
class ReaderApp : MultiDexApplication(), ApiHolderProvider, CommonApiProvider {

    private val cicerone = Cicerone.create()

    override val apiHolder = ApiHolderImpl(this, cicerone)

    override val commonApi: CommonApi
        get() = coreCommonComponent

    private val coreCommonComponent: CoreCommonComponent by lazy {
        DaggerCoreCommonComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
        NotificationUtils.initNotificationChannels(this)
        initLeakCanary()
        initLogger()
    }

    private fun initLeakCanary() {
        val newConfig = LeakCanary.config.copy(
            retainedVisibleThreshold = 2
        )
        LeakCanary.config = newConfig
    }

    private fun initLogger() {
        Log.init(BuildConfig.DEBUG)
    }
}