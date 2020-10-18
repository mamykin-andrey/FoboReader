package ru.mamykin.foboreader.app

import androidx.multidex.MultiDexApplication
import leakcanary.LeakCanary
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.binds
import org.koin.dsl.module
import ru.mamykin.foboreader.app.navigation.AppNavigator
import ru.mamykin.foboreader.app.navigation.featureNavigators
import ru.mamykin.foboreader.book_details.di.bookDetailsModule
import ru.mamykin.foboreader.common_book_info.di.commonBookInfoModule
import ru.mamykin.foboreader.core.di.coreModule
import ru.mamykin.foboreader.core.platform.NotificationUtils
import ru.mamykin.foboreader.my_books.di.myBooksModule
import ru.mamykin.foboreader.read_book.di.readBookModule
import ru.mamykin.foboreader.settings.di.settingsModule
import ru.mamykin.foboreader.store.di.booksStoreModule

@Suppress("unused")
class ReaderApp : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        initDi()
        NotificationUtils.initNotificationChannels(this)
        initLeakCanary()
    }

    private fun initDi() {
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(applicationContext)
            modules(
                listOf(
                    module { single { AppNavigator() }.binds(featureNavigators) },
                    coreModule,
                    commonBookInfoModule,
                    myBooksModule,
                    bookDetailsModule,
                    booksStoreModule,
                    settingsModule,
                    readBookModule
                )
            )
        }
    }

    private fun initLeakCanary() {
        val newConfig = LeakCanary.config.copy(
            retainedVisibleThreshold = 2
        )
        LeakCanary.config = newConfig
    }
}