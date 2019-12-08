package ru.mamykin.foboreader

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.mamykin.core.data.SettingsStorage
import ru.mamykin.core.di.appModule
import ru.mamykin.core.ui.UiUtils
import ru.mamykin.my_books.di.myBooksModule
import ru.mamykin.store.di.booksStoreModule

class ReaderApp : MultiDexApplication() {

    private val settingsStorage: SettingsStorage by inject()

    override fun onCreate() {
        super.onCreate()
        setupDi()
        setupTheme()
    }

    private fun setupTheme() {
        val nightModeEnabled = settingsStorage.isNightTheme
        UiUtils.enableNightMode(nightModeEnabled)
    }

    private fun setupDi() {
        startKoin {
            androidLogger()
            androidContext(this@ReaderApp)
            modules(listOf(
                    appModule,
                    myBooksModule,
                    booksStoreModule
            ))
        }
    }
}