package ru.mamykin.foboreader.core.di.component

import dagger.Component
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.core.di.modules.*
import javax.inject.Singleton

@Component(modules = [
    AppModule::class,
    NetworkModule::class,
    DatabaseModule::class,
    PreferencesModule::class,
    RepositoryModule::class
])
@Singleton
interface AppComponent {

    fun inject(application: ReaderApp)

    fun getBookDetailsComponent(bookDetailsModule: BookDetailsModule): BookDetailsComponent

    fun getDeviceBooksComponent(module: DeviceBooksModule): DeviceBooksComponent

    fun getDropboxBooksComponent(module: DropboxBooksModule): DropboxBooksComponent

    fun getMyBooksComponent(module: MyBooksModule): MyBooksComponent

    fun getReadBookComponent(module: ReadBookModule): ReadBookComponent

    fun getSettingsComponent(): SettingsComponent

    fun getBooksStoreComponent(): BooksStoreComponent
}