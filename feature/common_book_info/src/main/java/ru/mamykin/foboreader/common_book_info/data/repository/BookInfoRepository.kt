package ru.mamykin.foboreader.common_book_info.data.repository

import ru.mamykin.foboreader.common_book_info.data.database.BookInfoDao
import ru.mamykin.foboreader.common_book_info.data.model.BookInfoModel
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

class BookInfoRepository(
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
        bookInfoDao.update(
            BookInfoModel(
                id = bookInfo.id,
                filePath = bookInfo.filePath,
                genre = bookInfo.genre,
                coverUrl = bookInfo.coverUrl,
                author = bookInfo.author,
                title = bookInfo.title,
                languages = bookInfo.languages,
                date = bookInfo.date,
                currentPage = currentPage,
                totalPages = totalPages,
                lastOpen = bookInfo.lastOpen,
            )
        )
    }
}