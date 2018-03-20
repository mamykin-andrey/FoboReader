package ru.mamykin.foreignbooksreader.di

import javax.inject.Singleton

import dagger.Component
import ru.mamykin.foreignbooksreader.ReaderApp
import ru.mamykin.foreignbooksreader.di.modules.ApiModule
import ru.mamykin.foreignbooksreader.di.modules.AppModule
import ru.mamykin.foreignbooksreader.di.modules.DatabaseModule
import ru.mamykin.foreignbooksreader.di.modules.MappersModule
import ru.mamykin.foreignbooksreader.di.modules.PreferencesModule
import ru.mamykin.foreignbooksreader.presenters.AboutPresenter
import ru.mamykin.foreignbooksreader.presenters.BookDetailsPresenter
import ru.mamykin.foreignbooksreader.presenters.DeviceBooksPresenter
import ru.mamykin.foreignbooksreader.presenters.DropboxBooksPresenter
import ru.mamykin.foreignbooksreader.presenters.MainPresenter
import ru.mamykin.foreignbooksreader.presenters.StorePresenter
import ru.mamykin.foreignbooksreader.presenters.MyBooksPresenter
import ru.mamykin.foreignbooksreader.presenters.ReadBookPresenter
import ru.mamykin.foreignbooksreader.presenters.SettingsPresenter
import ru.mamykin.foreignbooksreader.ui.activities.BaseActivity
import ru.mamykin.foreignbooksreader.ui.adapters.BooksRecyclerAdapter
import ru.mamykin.foreignbooksreader.ui.adapters.BooksStoreRecyclerAdapter
import ru.mamykin.foreignbooksreader.ui.adapters.DropboxRecyclerAdapter
import ru.mamykin.foreignbooksreader.ui.adapters.FilesRecyclerAdapter
import ru.mamykin.foreignbooksreader.ui.adapters.PromotionsRecyclerAdapter
import ru.mamykin.foreignbooksreader.ui.fragments.MyBooksFragment
import ru.mamykin.foreignbooksreader.preferences.PreferencesManager

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Component(modules = arrayOf(AppModule::class, ApiModule::class, DatabaseModule::class, PreferencesModule::class, MappersModule::class))
@Singleton
interface AppComponent {
    fun inject(fragment: MyBooksFragment)

    fun inject(activity: BaseActivity)

    fun inject(adapter: FilesRecyclerAdapter)

    fun inject(adapter: DropboxRecyclerAdapter)

    fun inject(adapter: BooksRecyclerAdapter)

    fun inject(adapter: BooksStoreRecyclerAdapter)

    fun inject(adapter: PromotionsRecyclerAdapter)

    fun inject(manager: PreferencesManager)

    fun inject(presenter: BookDetailsPresenter)

    fun inject(presenter: DropboxBooksPresenter)

    fun inject(presenter: MyBooksPresenter)

    fun inject(presenter: SettingsPresenter)

    fun inject(presenter: ReadBookPresenter)

    fun inject(presenter: StorePresenter)

    fun inject(presenter: AboutPresenter)

    fun inject(presenter: DeviceBooksPresenter)

    fun inject(presenter: MainPresenter)

    fun inject(application: ReaderApp)
}