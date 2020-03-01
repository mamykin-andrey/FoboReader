package ru.mamykin.foboreader.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.mamykin.foboreader.core.di.coreModule
import ru.mamykin.foboreader.core.platform.NotificationUtils
import ru.mamykin.foboreader.my_books.di.myBooksModule
import ru.mamykin.foboreader.read_book.di.readBookModule
import ru.mamykin.foboreader.settings.di.settingsModule
import ru.mamykin.foboreader.store.di.booksStoreModule

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