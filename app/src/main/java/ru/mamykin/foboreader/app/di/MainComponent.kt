package ru.mamykin.foboreader.app.di

import dagger.Component
import ru.mamykin.foboreader.app.presentation.MainActivity
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class MainScope

@MainScope
@Component(dependencies = [NavigationApi::class, SettingsApi::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            navigationApi: NavigationApi,
            settingsApi: SettingsApi
        ): MainComponent
    }
}