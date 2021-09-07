package ru.mamykin.foboreader.app

import androidx.multidex.MultiDexApplication
import com.github.terrakok.cicerone.Cicerone
import leakcanary.LeakCanary
import ru.mamykin.foboreader.app.di.ApiHolderImpl
import ru.mamykin.foboreader.app.di.AppComponent
import ru.mamykin.foboreader.app.di.DaggerAppComponent
import ru.mamykin.foboreader.book_details.di.BookDetailsComponent
import ru.mamykin.foboreader.book_details.di.BookDetailsComponentHolder
import ru.mamykin.foboreader.core.di.api.ApiHolderProvider
import ru.mamykin.foboreader.core.platform.NotificationUtils
import ru.mamykin.foboreader.my_books.di.MyBooksComponent
import ru.mamykin.foboreader.my_books.di.MyBooksComponentHolder
import ru.mamykin.foboreader.read_book.di.ReadBookComponent
import ru.mamykin.foboreader.read_book.di.ReadBookComponentHolder
import ru.mamykin.foboreader.settings.di.SettingsComponent
import ru.mamykin.foboreader.settings.di.SettingsComponentHolder
import ru.mamykin.foboreader.store.di.BooksStoreComponent
import ru.mamykin.foboreader.store.di.BooksStoreComponentHolder

@Suppress("unused")
class ReaderApp : MultiDexApplication(),
    BooksStoreComponentHolder,
    SettingsComponentHolder,
    BookDetailsComponentHolder,
    MyBooksComponentHolder,
    ReadBookComponentHolder,
    ApiHolderProvider {

    private lateinit var appComponent: AppComponent
    private val cicerone = Cicerone.create()

    override val apiHolder = ApiHolderImpl(this, cicerone)

    override fun onCreate() {
        super.onCreate()
        initDi()
        NotificationUtils.initNotificationChannels(this)
        initLeakCanary()
    }

    private fun initDi() {
        appComponent = DaggerAppComponent.factory().create(this, cicerone)
    }

    private fun initLeakCanary() {
        val newConfig = LeakCanary.config.copy(
            retainedVisibleThreshold = 2
        )
        LeakCanary.config = newConfig
    }

    override fun booksStoreComponent(): BooksStoreComponent = appComponent.booksStoreComponent()

    override fun settingsComponent(): SettingsComponent = appComponent.settingsComponent()

    override fun bookDetailsComponent(bookId: Long): BookDetailsComponent {
        return appComponent.bookDetailsComponentFactory()
            .create(bookId)
    }

    override fun myBooksComponent(): MyBooksComponent = appComponent.myBooksComponent()

    override fun readBookComponent(bookId: Long): ReadBookComponent {
        return appComponent.readBookComponentFactory()
            .create(bookId)
    }
}