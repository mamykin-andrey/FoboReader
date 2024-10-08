package ru.mamykin.foboreader.book_details.details

import android.content.Context
import dagger.Module
import dagger.Provides
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDaoFactory
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository

@Module
class DatabaseModule {

    @Provides
    @BookDetailsScope
    fun provideBookInfoRepository(dao: BookInfoDao): BookInfoRepository = BookInfoRepository(dao)

    @Provides
    @BookDetailsScope
    fun provideBookInfoDao(context: Context): BookInfoDao = BookInfoDaoFactory.create(context)
}