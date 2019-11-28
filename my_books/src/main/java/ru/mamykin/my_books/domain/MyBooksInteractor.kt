package ru.mamykin.my_books.domain

import ru.mamykin.core.data.database.BookDao
import ru.mamykin.core.data.model.FictionBook
import ru.mamykin.my_books.data.MyBooksRepository
import javax.inject.Inject

class MyBooksInteractor @Inject constructor(
        private val repositoryMy: MyBooksRepository
) {
    suspend fun removeBook(bookPath: String) {
        repositoryMy.removeBook(bookPath)
    }

    suspend fun getBooks(searchQuery: String, sortOrder: BookDao.SortOrder): List<FictionBook> {
        return repositoryMy.getBooks(searchQuery, sortOrder)
    }

    suspend fun getBook(bookPath: String): FictionBook {
        return repositoryMy.getBook(bookPath)
    }

    suspend fun getBookFilePath(bookPath: String): String {
        return repositoryMy.getBook(bookPath).filePath
    }
}