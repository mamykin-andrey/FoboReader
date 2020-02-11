package ru.mamykin.my_books.domain.my_books

import ru.mamykin.my_books.data.MyBooksRepository
import ru.mamykin.my_books.domain.model.BookInfo
import java.io.File
import java.lang.IllegalArgumentException

class MyBooksInteractor constructor(
        private val repository: MyBooksRepository,
        private val booksScanner: BookFilesScanner
) {
    private var scanned = false

    suspend fun scanNewFiles(force: Boolean = false) {
        if (!force && scanned) return

        booksScanner.scan()
        scanned = true
    }

    suspend fun getBooks(query: String? = null, sortOrder: SortOrder? = null): List<BookInfo> {
        scanNewFiles()

        val books = if (query.isNullOrEmpty())
            repository.getBooks()
        else
            repository.findBooks(query)

        return sortBooks(books, sortOrder)
    }

    private fun sortBooks(books: List<BookInfo>, sortOrder: SortOrder?): List<BookInfo> {
        // TODO: sort books by sort order
        return books
    }

    suspend fun getBookInfo(id: Long): BookInfo? {
        return repository.getBook(id)
    }

    suspend fun removeBook(id: Long) {
        val bookInfo = repository.getBook(id)
                ?: throw IllegalArgumentException("Wrong book id: $id!")

        repository.removeBook(bookInfo.id)
        File(bookInfo.filePath).delete()
    }
}