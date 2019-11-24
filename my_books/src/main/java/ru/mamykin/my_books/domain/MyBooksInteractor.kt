package ru.mamykin.my_books.domain

import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.data.model.FictionBook
import ru.mamykin.my_books.data.BooksRepository
import javax.inject.Inject

class MyBooksInteractor @Inject constructor(
        private val repository: BooksRepository
) {
    suspend fun removeBook(bookPath: String) {
        repository.removeBook(bookPath)
    }

    suspend fun getBooks(searchQuery: String, sortOrder: BookDao.SortOrder): List<FictionBook> {
        return repository.getBooks(searchQuery, sortOrder)
    }

    suspend fun getBook(bookPath: String): FictionBook {
        return repository.getBook(bookPath)
    }

    suspend fun getBookFilePath(bookPath: String): String {
        return repository.getBook(bookPath).filePath
    }
}