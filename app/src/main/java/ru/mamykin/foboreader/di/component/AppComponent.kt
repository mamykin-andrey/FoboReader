package ru.mamykin.foboreader.di.component

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

    fun getBookDetailsComponent(bookDetailsModule: BookDetailsModule): BookDetailsComponent

    fun getDeviceBooksComponent(): DeviceBooksComponent

    fun getDropboxBooksComponent(): DropboxBooksComponent

    fun getMyBooksComponent(): MyBooksComponent

    fun getReadBookComponent(module: ReadBookModule): ReadBookComponent

    fun getSettingsComponent(): SettingsComponent
}