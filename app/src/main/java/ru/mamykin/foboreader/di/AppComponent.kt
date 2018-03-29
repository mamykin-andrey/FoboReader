package ru.mamykin.foboreader.di

import dagger.Component
import ru.mamykin.foboreader.ReaderApp
import ru.mamykin.foboreader.di.modules.*
import ru.mamykin.foboreader.data.storage.PreferencesManager
import ru.mamykin.foboreader.presentation.about.AboutPresenter
import ru.mamykin.foboreader.presentation.bookdetails.BookDetailsPresenter
import ru.mamykin.foboreader.presentation.devicebooks.DeviceBooksPresenter
import ru.mamykin.foboreader.presentation.dropbox.DropboxBooksPresenter
import ru.mamykin.foboreader.presentation.main.MainPresenter
import ru.mamykin.foboreader.presentation.mybooks.MyBooksPresenter
import ru.mamykin.foboreader.presentation.readbook.ReadBookPresenter
import ru.mamykin.foboreader.presentation.settings.SettingsPresenter
import ru.mamykin.foboreader.presentation.store.BooksStorePresenter
import ru.mamykin.foboreader.ui.global.BaseActivity
import ru.mamykin.foboreader.ui.mybooks.list.BooksRecyclerAdapter
import ru.mamykin.foboreader.ui.store.list.BooksStoreRecyclerAdapter
import ru.mamykin.foboreader.ui.dropbox.list.DropboxRecyclerAdapter
import ru.mamykin.foboreader.ui.devicebooks.list.FilesRecyclerAdapter
import ru.mamykin.foboreader.ui.mybooks.MyBooksFragment
import javax.inject.Singleton

/**
 * Creation date: 5/29/2017
 * Creation time: 11:39 AM
 * @author Andrey Mamykin(mamykin_av)
 */
@Component(modules = [(AppModule::class), (ApiModule::class), (DatabaseModule::class), (PreferencesModule::class), (MappersModule::class)])
@Singleton
interface AppComponent {
    fun inject(fragment: MyBooksFragment)

    fun inject(activity: BaseActivity)

    fun inject(adapter: FilesRecyclerAdapter)

    fun inject(adapter: DropboxRecyclerAdapter)

    fun inject(adapter: BooksRecyclerAdapter)

    fun inject(adapter: BooksStoreRecyclerAdapter)

    fun inject(manager: PreferencesManager)

    fun inject(presenter: BookDetailsPresenter)

    fun inject(presenter: DropboxBooksPresenter)

    fun inject(presenter: MyBooksPresenter)

    fun inject(presenter: SettingsPresenter)

    fun inject(presenter: ReadBookPresenter)

    fun inject(presenter: BooksStorePresenter)

    fun inject(presenter: AboutPresenter)

    fun inject(presenter: DeviceBooksPresenter)

    fun inject(presenter: MainPresenter)

    fun inject(application: ReaderApp)
}