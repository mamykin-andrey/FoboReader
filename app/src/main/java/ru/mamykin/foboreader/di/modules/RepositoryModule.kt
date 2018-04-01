package ru.mamykin.foboreader.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.database.BookDatabaseHelper
import ru.mamykin.foboreader.data.network.BooksStoreService
import ru.mamykin.foboreader.data.network.YandexTranslateService
import ru.mamykin.foboreader.data.repository.*
import ru.mamykin.foboreader.data.storage.PreferencesManager
import ru.mamykin.foboreader.entity.mapper.FolderToFilesListMapper
import javax.inject.Singleton

@Module
@Singleton
class RepositoryModule {

    @Provides
    @Singleton
    fun provideBooksRepository(bookDbHelper: BookDatabaseHelper): BooksRepository {
        return BooksRepository(bookDbHelper)
    }

    @Provides
    @Singleton
    fun provideDeviceBooksRepository(): DeviceBooksRepository {
        return DeviceBooksRepository()
    }

    @Provides
    @Singleton
    fun provideDropboxBooksRepository(
            prefManager: PreferencesManager,
            mapper: FolderToFilesListMapper): DropboxBooksRepository {

        return DropboxBooksRepository(prefManager, mapper)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(prefManager: PreferencesManager): SettingsRepository {
        return SettingsRepository(prefManager)
    }

    @Provides
    @Singleton
    fun provideStoreBooksRepository(booksStoreService: BooksStoreService): StoreBooksRepository {
        return StoreBooksRepository(booksStoreService)
    }

    @Provides
    @Singleton
    fun provideTranslateRepository(
            yandexTranslateService: YandexTranslateService,
            context: Context): TranslateRepository {

        return TranslateRepository(yandexTranslateService, context)
    }
}