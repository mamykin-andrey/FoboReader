package ru.mamykin.book_details.data

import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.data.model.FictionBook
import javax.inject.Inject

class BookDetailsRepository @Inject constructor(
        private val bookDao: BookDao
) {
    suspend fun getBookInfo(bookPath: String): FictionBook {
        return bookDao.getBook(bookPath)!!
    }
}