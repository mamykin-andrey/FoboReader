package ru.mamykin.foboreader.common_book_info.data.repository

import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import javax.inject.Inject

class BookInfoRepository @Inject constructor(
    private val bookInfoDao: BookInfoDao
) {
    suspend fun getBooks(): List<BookInfo> {
        return bookInfoDao.getBooks()
            .map { it.toDomainModel() }
    }

    suspend fun getBookInfo(id: Long): BookInfo {
        return bookInfoDao.getBook(id)
            .toDomainModel()
    }

    suspend fun removeBook(id: Long) {
        bookInfoDao.remove(id)
    }

    suspend fun updateBookInfo(bookId: Long, currentPage: Int, totalPages: Int) {
        val bookInfo = bookInfoDao.getBook(bookId)
        val updatedBookInfo = bookInfo.copy(
            currentPage = currentPage,
            totalPages = totalPages
        )
        bookInfoDao.update(updatedBookInfo)
    }
}