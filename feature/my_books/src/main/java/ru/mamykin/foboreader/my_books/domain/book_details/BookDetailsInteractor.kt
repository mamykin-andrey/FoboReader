package ru.mamykin.foboreader.my_books.domain.book_details

import ru.mamykin.foboreader.core.data.BookInfoRepository
import ru.mamykin.foboreader.core.domain.model.BookInfo

class BookDetailsInteractor constructor(
        private val repository: BookInfoRepository
) {
    suspend fun getBookInfo(id: Long): BookInfo? {
        return repository.getBook(id)
    }
}