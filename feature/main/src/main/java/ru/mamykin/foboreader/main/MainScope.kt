package ru.mamykin.foboreader.main

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.MainApi
import ru.mamykin.foboreader.core.di.module.CoroutinesModule
import javax.inject.Scope

// TODO: Rename to MainScreen

@Scope
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
internal annotation class MainScope

@MainScope
@Component(
    dependencies = [CommonApi::class, MainApi::class],
    modules = [CoroutinesModule::class]
)
internal interface MainComponent {

    fun inject(fragment: MainFragment)

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            mainApi: MainApi,
        ): MainComponent
    }
}