package ru.mamykin.foboreader.app.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.NavigationApi

class ApiHolderImpl(
    private val context: Context,
    private val cicerone: Cicerone<Router>
) : ApiHolder {

    private val apiMap = HashMap<Any, Any>()

    override fun navigationApi(): NavigationApi {
        return apiMap[NavigationApi::class] as? NavigationApi ?: run {
            DaggerAppComponent.factory().create(context, cicerone).also {
                apiMap[NavigationApi::class] = it
            }
        }
    }
}