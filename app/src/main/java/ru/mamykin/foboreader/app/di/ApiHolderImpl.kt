package ru.mamykin.foboreader.app.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.mamykin.foboreader.core.di.api.ApiHolder
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.NetworkApi

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

    override fun networkApi(): NetworkApi {
        return apiMap[NetworkApi::class] as? NetworkApi ?: run {
            DaggerAppComponent.factory().create(context, cicerone).also {
                apiMap[NetworkApi::class] = it
            }
        }
    }

    override fun commonApi(): CommonApi {
        return apiMap[CommonApi::class] as? CommonApi ?: run {
            DaggerAppComponent.factory().create(context, cicerone).also {
                apiMap[CommonApi::class] = it
            }
        }
    }
}