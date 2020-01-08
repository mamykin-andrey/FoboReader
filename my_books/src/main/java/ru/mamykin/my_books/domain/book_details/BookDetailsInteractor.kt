package ru.mamykin.my_books.domain.book_details

import ru.mamykin.my_books.data.MyBooksRepository
import ru.mamykin.my_books.domain.model.BookInfo

class BookDetailsInteractor constructor(
        private val repository: MyBooksRepository
) {
    suspend fun getBookInfo(id: Long): BookInfo? {
        return repository.getBook(id)
    }
}