package ru.mamykin.foboreader.domain.bookdetails

import ru.mamykin.foboreader.data.repository.books.BooksRepository
import ru.mamykin.foboreader.domain.entity.FictionBook
import javax.inject.Inject

class BookDetailsInteractor @Inject constructor(
        private val repository: BooksRepository
) {
    suspend fun getBookInfo(bookPath: String): FictionBook {
        return repository.getBookInfo(bookPath)
    }
}