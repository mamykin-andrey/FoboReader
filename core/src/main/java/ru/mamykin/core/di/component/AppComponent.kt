package ru.mamykin.core.di.component

import dagger.Component
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.di.module.AppModule
import ru.mamykin.core.di.module.DatabaseModule
import ru.mamykin.core.ui.BaseActivity
import ru.mamykin.core.ui.BaseFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    DatabaseModule::class
])
interface AppComponent {

    fun settingsStorage(): SettingsStorage

    fun booksDao(): BookDao

    fun inject(activity: BaseActivity)

    fun inject(fragment: BaseFragment)
}