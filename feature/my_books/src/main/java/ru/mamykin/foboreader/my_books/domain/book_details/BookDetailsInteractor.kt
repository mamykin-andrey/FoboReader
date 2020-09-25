package ru.mamykin.foboreader.my_books.domain.book_details

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

class BookDetailsInteractor constructor(
    private val repository: BookInfoRepository
) {
    suspend fun getBookInfo(id: Long): BookInfo? {
        return repository.getBookInfo(id)
    }
}