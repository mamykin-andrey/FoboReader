package ru.mamykin.foboreader.my_books.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import java.io.File

@ExperimentalCoroutinesApi
@FlowPreview
class MyBooksInteractor constructor(
    private val repository: BookInfoRepository,
    private val booksScanner: BookFilesScanner
) {
    private val booksChannel = BroadcastChannel<List<BookInfo>>(1)
    val booksFlow get() = booksChannel.asFlow()

    private var searchQuery: String = ""
    private var sortOrder: SortOrder = SortOrder.ByName

    private var scanned = false

    suspend fun loadBooks() {
        scanBooks()
        updateBooks()
    }

    suspend fun scanBooks() {
        scanBooks(force = true)
        updateBooks()
    }

    private suspend fun scanBooks(force: Boolean = false) {
        if (!force && scanned) return
        booksScanner.scan()
        scanned = true
    }

    suspend fun removeBook(id: Long) {
        val bookInfo = repository.getBookInfo(id)
            ?: throw IllegalArgumentException("Can't find books with id to delete: $id!")
        repository.removeBook(bookInfo.id)
            .also { File(bookInfo.filePath).delete() }
        updateBooks()
    }

    suspend fun sortBooks(sortOrder: SortOrder) {
        this.sortOrder = sortOrder
        updateBooks()
    }

    suspend fun filterByQuery(query: String) {
        this.searchQuery = query
        updateBooks()
    }

    private suspend fun updateBooks() {
        val books = searchQuery.takeIf { it.isNotEmpty() }
            ?.let { repository.findBooks(it) }
            ?: repository.getBooks()

        val sortedBooks = books.sortedWith(BooksComparatorFactory().create(sortOrder))
        booksChannel.send(sortedBooks)
    }
}