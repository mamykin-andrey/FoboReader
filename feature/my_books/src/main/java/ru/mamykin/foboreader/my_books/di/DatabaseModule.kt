package ru.mamykin.foboreader.my_books.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDaoFactory
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository

@Module
class DatabaseModule {

    @Provides
    @MyBooksScope
    fun provideBookInfoRepository(dao: BookInfoDao): BookInfoRepository = BookInfoRepository(dao)

    @Provides
    @MyBooksScope
    fun provideBookInfoDao(context: Context): BookInfoDao = BookInfoDaoFactory.create(context)
}