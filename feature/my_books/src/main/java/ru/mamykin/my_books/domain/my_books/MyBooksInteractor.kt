package ru.mamykin.my_books.domain.my_books

import ru.mamykin.my_books.data.MyBooksRepository
import ru.mamykin.my_books.domain.model.BookInfo

class MyBooksInteractor constructor(
        private val repository: MyBooksRepository,
        private val booksScanner: BookFilesScanner
) {
    private var scanned = false

    suspend fun getBooks(query: String? = null, sortOrder: SortOrder? = null): List<BookInfo> {
        booksScanner.takeIf { !scanned }?.scan()

        //val books = if (query.isNullOrEmpty())
        val books = repository.getBooks()
//        else
//            repository.findBooks(query)

        return books

        //return sortBooks(books, sortOrder)
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