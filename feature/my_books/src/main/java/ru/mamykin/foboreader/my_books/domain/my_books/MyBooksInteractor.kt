package ru.mamykin.foboreader.my_books.domain.my_books

import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.model.BookInfo
import java.io.File

class MyBooksInteractor constructor(
        private val repository: MyBooksRepository,
        private val booksScanner: BookFilesScanner
) {
    var searchQuery: String = ""
    var sortOrder: SortOrder = SortOrder.ByName

    private var scanned = false

    suspend fun scanNewFiles(force: Boolean = false) {
        if (!force && scanned) return

        booksScanner.scan()
        scanned = true
    }

    suspend fun getBooks(query: String? = null): List<BookInfo> {
        scanNewFiles()
        return sortBooks(repository.getBooks(query), sortOrder)
    }

    private fun sortBooks(books: List<BookInfo>, sortOrder: SortOrder?): List<BookInfo> {
        // TODO:
        return books
    }

    suspend fun removeBook(id: Long) {
        val bookInfo = repository.getBook(id) ?: run {
            Log.error("Wrong book id: $id!")
            return
        }
        repository.removeBook(bookInfo.id)
        File(bookInfo.filePath).delete()
    }
}