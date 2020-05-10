package ru.mamykin.foboreader.my_books.domain.my_books

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import ru.mamykin.foboreader.core.data.BookInfoRepository
import ru.mamykin.foboreader.core.domain.model.BookInfo
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
        booksChannel.send(getSortedBooks())
    }

    private suspend fun getSortedBooks(): List<BookInfo> = repository.getBooks(searchQuery)
            .sortedWith(Comparator { o1, o2 ->
                when (sortOrder) {
                    SortOrder.ByName -> o1.title.compareTo(o2.title)
                    SortOrder.ByReaded -> o1.currentPage.compareTo(o2.currentPage)
                    SortOrder.ByDate -> o1.date?.compareTo(o2.date) ?: 0
                }
            })
}