package ru.mamykin.foboreader.app.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.MainApi
import ru.mamykin.foboreader.core.di.api.NetworkApi
import ru.mamykin.foboreader.core.di.api.SettingsApi

// TODO: Move to the AppComponent
class ApiHolderImpl(
    private val context: Context,
    private val cicerone: Cicerone<Router>
) : ApiHolder {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(context, cicerone)
    }

    override fun networkApi(): NetworkApi = appComponent

    override fun settingsApi(): SettingsApi = appComponent

    override fun mainApi(): MainApi = appComponent

    override fun commonApi(): CommonApi = appComponent
}