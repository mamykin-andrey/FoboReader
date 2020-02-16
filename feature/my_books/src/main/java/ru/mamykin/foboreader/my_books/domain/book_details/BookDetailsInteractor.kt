package ru.mamykin.foboreader.my_books.domain.book_details

import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.model.BookInfo

class BookDetailsInteractor constructor(
        private val repository: MyBooksRepository
) {
    suspend fun getBookInfo(id: Long): BookInfo? {
        return repository.getBook(id)
    }
}