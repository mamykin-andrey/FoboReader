package ru.mamykin.foboreader.app

import androidx.multidex.MultiDexApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.mamykin.foboreader.app.navigation.NavigatorImpl
import ru.mamykin.foboreader.common_book_info.di.commonBookInfoModule
import ru.mamykin.foboreader.core.di.coreModule
import ru.mamykin.foboreader.core.platform.Navigator
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
    }

    private fun initDi() {
        startKoin {
            androidLogger()
            androidContext(applicationContext)
            modules(
                listOf(
                    module { single<Navigator> { NavigatorImpl() } },
                    coreModule,
                    commonBookInfoModule,
                    myBooksModule,
                    booksStoreModule,
                    settingsModule,
                    readBookModule
                )
            )
        }
    }
}