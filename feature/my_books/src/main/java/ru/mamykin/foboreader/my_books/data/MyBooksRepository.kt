package ru.mamykin.foboreader.my_books.data

import android.content.Context
import android.os.FileObserver
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.getExternalMediaDir
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.my_books.domain.helper.BookFilesScanner
import ru.mamykin.foboreader.my_books.domain.helper.BooksComparatorFactory
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import kotlin.properties.Delegates

class MyBooksRepository(
    private val repository: BookInfoRepository,
    private val booksScanner: BookFilesScanner,
    private val context: Context
) {
    private var searchQuery: String = ""
    private var sortOrder: SortOrder = SortOrder.ByName

    private val booksChannel = ConflatedBroadcastChannel<List<BookInfo>>()
    val booksFlow = booksChannel.asFlow()

    private var books by Delegates.observable(emptyList<BookInfo>()) { _, _, new ->
        booksChannel.offer(new)
    }
    private var allBooks = emptyList<BookInfo>()

    suspend fun sort(sortOrder: SortOrder) {
        this.sortOrder = sortOrder
        updateBooks()
    }

    suspend fun filter(query: String) {
        this.searchQuery = query
        updateBooks()
    }

    private suspend fun updateBooks() {
        this.allBooks = repository.getBooks()
        this.books = allBooks
            .filter { it.title.contains(searchQuery) } // TODO: real filter
            .sortedWith(BooksComparatorFactory().create(sortOrder))
    }

    suspend fun scanBooks() {
        fileChangesFlow()
            .onStart { emit(Unit) }
            .collect { loadNewBooks() }
    }

    suspend fun getBooks(): Flow<List<BookInfo>> {
        return booksFlow
    }

    private suspend fun loadNewBooks() {
        booksScanner.scan()
        updateBooks()
    }

    private fun fileChangesFlow(): Flow<Unit> = callbackFlow {
        val externalMediaDir = context.getExternalMediaDir() ?: run {
            Log.error("Can't open media directory!")
            return@callbackFlow
        }
        val changesMask = FileObserver.CREATE or FileObserver.DELETE or FileObserver.DELETE_SELF

        val fileObserver = object : FileObserver(externalMediaDir, changesMask) {
            override fun onEvent(event: Int, file: String?) {
                sendBlocking(Unit)
            }
        }
        fileObserver.startWatching()
        awaitClose { fileObserver.stopWatching() }
    }
}