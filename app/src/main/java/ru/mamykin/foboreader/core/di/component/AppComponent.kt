package ru.mamykin.foboreader.core.di.component

import dagger.Component
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.core.di.modules.*
import ru.mamykin.foboreader.core.ui.BaseActivity
import ru.mamykin.foboreader.core.ui.BaseFragment
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
interface AppComponent {

    fun inject(application: ReaderApp)

    fun inject(activity: BaseActivity)

    fun inject(fragment: BaseFragment)

    fun getBookDetailsComponent(bookDetailsModule: BookDetailsModule): BookDetailsComponent

    fun getDeviceBooksComponent(): DeviceBooksComponent

    fun getReadBookComponent(readBookModule: ReadBookModule): ReadBookComponent

    fun getSettingsComponent(): SettingsComponent

    fun getBooksStoreComponent(): BooksStoreComponent

    fun getMyBooksComponent(): MyBooksComponent
}