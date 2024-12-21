package ru.mamykin.foboreader.my_books.list

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import javax.inject.Inject

internal class LoadMyBooks @Inject constructor(
    private val myBooksRepository: MyBooksRepository,
    private val booksScanner: BookFilesScanner,
) {
    suspend fun execute(): List<BookInfo> = coroutineScope {
        val allStoredBooks = booksScanner.scan()
        launch { myBooksRepository.updateBooks(allStoredBooks) }
        return@coroutineScope allStoredBooks
    }
}