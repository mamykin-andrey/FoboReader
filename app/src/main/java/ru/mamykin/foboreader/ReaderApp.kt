package ru.mamykin.foboreader

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.mamykin.core.di.coreModule
import ru.mamykin.my_books.di.myBooksModule
import ru.mamykin.read_book.di.readBookModule
import ru.mamykin.settings.di.settingsModule
import ru.mamykin.store.di.booksStoreModule

class ReaderApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initDi()
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