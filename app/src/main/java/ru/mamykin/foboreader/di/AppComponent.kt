package ru.mamykin.foboreader.di

import dagger.Component
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.di.modules.*
import javax.inject.Singleton

@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    PreferencesModule::class,
    MappersModule::class
])
@Singleton
interface AppComponent {

    fun inject(application: ReaderApp)
}