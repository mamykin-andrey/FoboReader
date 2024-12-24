package ru.mamykin.foboreader.main

import dagger.Component
import ru.mamykin.foboreader.core.di.api.CommonApi
import ru.mamykin.foboreader.core.di.api.MainApi
import ru.mamykin.foboreader.core.di.module.CoroutinesModule
import ru.mamykin.foboreader.core.navigation.TabComposableProvider
import javax.inject.Scope

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

    fun viewModel(): MainViewModel

    fun tabComposableProvider(): TabComposableProvider

    @Component.Factory
    interface Factory {

        fun create(
            commonApi: CommonApi,
            mainApi: MainApi,
        ): MainComponent
    }
}