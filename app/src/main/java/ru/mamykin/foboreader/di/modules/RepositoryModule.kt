package ru.mamykin.foboreader.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.core.data.BookParser
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.read_book.data.BooksRepository
import ru.mamykin.store.data.BooksStoreService
import ru.mamykin.read_book.data.YandexTranslateService
import ru.mamykin.store.data.BooksStoreRepository
import ru.mamykin.read_book.data.TranslateRepository
import javax.inject.Singleton

@Module
@Singleton
class RepositoryModule {

    @Provides
    @Singleton
    fun provideBooksRepository(bookDao: BookDao, bookParser: BookParser) =
            BooksRepository(bookDao, bookParser)

    @Provides
    @Singleton
    fun provideStoreBooksRepository(booksStoreService: BooksStoreService) =
            BooksStoreRepository(booksStoreService)

    @Provides
    @Singleton
    fun provideTranslateRepository(translationService: YandexTranslateService) =
            TranslateRepository(translationService)
}