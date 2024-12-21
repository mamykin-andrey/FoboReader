package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.model.toDatabaseModel
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import javax.inject.Inject

internal class MyBooksRepository @Inject constructor(
    private val bookInfoDao: BookInfoDao,
) {
    suspend fun getBooks(): List<BookInfo> {
        return bookInfoDao.getBooks().map { it.toDomainModel() }
    }

    suspend fun updateBooks(books: List<BookInfo>) {
        bookInfoDao.insertAll(books.map { it.toDatabaseModel() })
    }
}