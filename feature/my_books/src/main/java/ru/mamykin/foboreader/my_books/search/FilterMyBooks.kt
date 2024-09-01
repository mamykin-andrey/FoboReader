package ru.mamykin.foboreader.my_books.search

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.list.MyBooksRepository
import javax.inject.Inject

internal class FilterMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    suspend fun execute(searchQuery: String): List<BookInfo> {
        return repository.filterBooks(searchQuery)
    }
}