package ru.mamykin.foboreader.core.di.component

import dagger.Component
import ru.mamykin.core.di.DependenciesProvider
import ru.mamykin.core.di.modules.DatabaseModule
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.core.di.modules.*
import javax.inject.Singleton

@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    PreferencesModule::class,
    RepositoryModule::class,
    ViewModelModule::class
])
@Singleton
interface AppComponent : DependenciesProvider {

    fun inject(application: ReaderApp)
}