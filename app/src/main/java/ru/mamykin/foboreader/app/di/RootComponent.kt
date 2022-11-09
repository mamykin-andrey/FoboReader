package ru.mamykin.foboreader.app.di

import dagger.Component
import ru.mamykin.foboreader.app.presentation.RootActivity
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.NavigationApi
import ru.mamykin.foboreader.core.di.api.SettingsApi
import javax.inject.Scope

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class RootScope

@RootScope
@Component(
    dependencies = [
        NavigationApi::class,
        SettingsApi::class,
        CommonApi::class,
    ]
)
internal interface RootComponent {

    fun inject(activity: RootActivity)

    @Component.Factory
    interface Factory {

        fun create(
            navigationApi: NavigationApi,
            settingsApi: SettingsApi,
            commonApi: CommonApi,
        ): RootComponent
    }
}