package ru.mamykin.foboreader.app.di

import android.content.Context
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import ru.mamykin.foboreader.core.di.api.*

class ApiHolderImpl(
    private val context: Context,
    private val cicerone: Cicerone<Router>
) : ApiHolder {

    private val apiMap = HashMap<Any, Any>()

    private fun createOrGetAppComponent(): AppComponent {
        return apiMap[AppComponent::class] as? AppComponent ?: run {
            DaggerAppComponent.factory().create(context, cicerone).also {
                apiMap[AppComponent::class] = it
            }
        }
    }

    override fun navigationApi(): NavigationApi = createOrGetAppComponent()

    override fun networkApi(): NetworkApi = createOrGetAppComponent()

    override fun commonApi(): CommonApi = createOrGetAppComponent()

    override fun settingsApi(): SettingsApi = createOrGetAppComponent()
}