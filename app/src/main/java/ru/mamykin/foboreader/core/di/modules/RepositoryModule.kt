package ru.mamykin.foboreader.core.di.modules

import dagger.Module
import dagger.Provides
import ru.mamykin.core.data.BookParser
import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.data.repository.books.BooksRepository
import ru.mamykin.store.data.BooksStoreService
import ru.mamykin.foboreader.data.network.YandexTranslateService
import ru.mamykin.store.data.BooksStoreRepository
import ru.mamykin.foboreader.data.repository.translate.TranslateRepository
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