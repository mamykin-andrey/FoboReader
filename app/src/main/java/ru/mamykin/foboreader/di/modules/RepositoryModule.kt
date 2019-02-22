package ru.mamykin.foboreader.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.data.database.BookDao
import ru.mamykin.foboreader.data.network.BooksStoreService
import ru.mamykin.foboreader.data.network.YandexTranslateService
import ru.mamykin.foboreader.data.repository.books.BookParser
import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.data.repository.booksstore.BooksStoreRepository
import ru.mamykin.foboreader.data.repository.dropboxbooks.DropboxBooksRepository
import ru.mamykin.foboreader.data.repository.dropboxbooks.DropboxBooksStorage
import ru.mamykin.foboreader.data.repository.settings.SettingsRepository
import ru.mamykin.foboreader.data.repository.settings.SettingsStorage
import ru.mamykin.foboreader.data.repository.translate.TranslateRepository
import ru.mamykin.foboreader.entity.mapper.FolderToFilesListMapper
import javax.inject.Singleton

@Module
@Singleton
class RepositoryModule {

    @Provides
    @Singleton
    fun provideBooksRepository(bookDao: BookDao, bookParser: BookParser): BooksRepository {
        return BooksRepository(bookDao, bookParser)
    }

    @Provides
    @Singleton
    fun provideDropboxBooksRepository(
            storage: DropboxBooksStorage,
            mapper: FolderToFilesListMapper): DropboxBooksRepository {

        return DropboxBooksRepository(storage, mapper)
    }

    @Provides
    @Singleton
    fun provideSettingsRepository(storage: SettingsStorage): SettingsRepository {
        return SettingsRepository(storage)
    }

    @Provides
    @Singleton
    fun provideStoreBooksRepository(booksStoreService: BooksStoreService): BooksStoreRepository {
        return BooksStoreRepository(booksStoreService)
    }

    @Provides
    @Singleton
    fun provideTranslateRepository(
            yandexTranslateService: YandexTranslateService,
            context: Context): TranslateRepository {

        return TranslateRepository(yandexTranslateService, context)
    }
}