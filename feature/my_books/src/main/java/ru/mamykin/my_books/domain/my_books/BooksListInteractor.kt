package ru.mamykin.my_books.domain.my_books

import ru.mamykin.my_books.data.MyBooksRepository
import ru.mamykin.my_books.domain.model.BookInfo

class BooksListInteractor constructor(
        private val repository: MyBooksRepository
) {
    suspend fun getBooks(query: String? = null, sortOrder: SortOrder? = null): List<BookInfo> {
        val books = if (query.isNullOrEmpty())
            repository.getBooks()
        else
            repository.findBooks(query)

        return sortBooks(books, sortOrder)
    }

    private fun sortBooks(books: List<BookInfo>, sortOrder: SortOrder?): List<BookInfo> {
        // TODO: SORT
        return books
    }

    suspend fun getBookInfo(id: Long): BookInfo? {
        return repository.getBook(id)
    }

    suspend fun removeBook(id: Long) {
        repository.removeBook(id)
    }
}