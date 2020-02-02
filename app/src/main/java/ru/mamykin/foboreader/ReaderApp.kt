package ru.mamykin.foboreader

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.mamykin.core.di.coreModule
import ru.mamykin.core.platform.NotificationUtils
import ru.mamykin.my_books.di.myBooksModule
import ru.mamykin.read_book.di.readBookModule
import ru.mamykin.settings.di.settingsModule
import ru.mamykin.store.di.booksStoreModule

@Suppress("unused")
class ReaderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDi()
        NotificationUtils.initNotificationChannels(this)
    }

    private fun initDi() {
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(listOf(
                    coreModule,
                    myBooksModule,
                    booksStoreModule,
                    settingsModule,
                    readBookModule
            ))
        }
    }
}