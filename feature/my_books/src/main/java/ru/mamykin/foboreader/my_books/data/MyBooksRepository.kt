package ru.mamykin.foboreader.my_books.data

import android.content.Context
import android.os.FileObserver
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.*
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.getExternalMediaDir
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.my_books.domain.helper.BookFilesScanner
import ru.mamykin.foboreader.my_books.domain.helper.BooksComparatorFactory
import ru.mamykin.foboreader.my_books.domain.model.SortOrder

class MyBooksRepository(
    private val repository: BookInfoRepository,
    private val booksScanner: BookFilesScanner,
    private val context: Context
) {
    private var allBooks = emptyList<BookInfo>()
    private val booksStateChannel = ConflatedBroadcastChannel(BooksState(loadNew = true))

    fun getBooks(): Flow<List<BookInfo>> = flow {
        booksStateChannel.consumeEach { state ->
            if (state.loadNew) {
                booksScanner.scan()
                allBooks = repository.getBooks()
            }
            emit(getSortedAndFilteredBooks(allBooks, state.searchQuery, state.sortOrder))
        }
    }

    private fun getSortedAndFilteredBooks(
        books: List<BookInfo>,
        searchQuery: String,
        sortOrder: SortOrder
    ): List<BookInfo> {
        return books.filter { it.containsText(searchQuery) }
            .sortedWith(BooksComparatorFactory().create(sortOrder))
    }

    private fun updateState(createNew: (BooksState) -> BooksState) {
        booksStateChannel.offer(createNew(booksStateChannel.value))
    }

    fun sort(sortOrder: SortOrder) {
        updateState {
            it.copy(sortOrder = sortOrder, loadNew = false)
        }
    }

    fun filter(searchQuery: String) {
        updateState {
            it.copy(searchQuery = searchQuery, loadNew = false)
        }
    }

    suspend fun scanBooks() {
        fileChangesFlow().collect {
            updateState {
                it.copy(loadNew = true)
            }
        }
    }

    private fun fileChangesFlow(): Flow<Unit> = callbackFlow {
        val externalMediaDir = context.getExternalMediaDir() ?: run {
            Log.error("Can't open media directory!")
            return@callbackFlow
        }

        val fileObserver = object : FileObserver(externalMediaDir, CREATE or DELETE) {
            override fun onEvent(event: Int, file: String?) {
                sendBlocking(Unit)
            }
        }
        fileObserver.startWatching()
        awaitClose { fileObserver.stopWatching() }
    }

    data class BooksState(
        val loadNew: Boolean = true,
        val sortOrder: SortOrder = SortOrder.ByName,
        val searchQuery: String = ""
    )
}