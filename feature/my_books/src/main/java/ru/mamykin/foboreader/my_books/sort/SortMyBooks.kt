package ru.mamykin.foboreader.my_books.sort

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.list.MyBooksRepository
import javax.inject.Inject

internal class SortMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    suspend fun execute(sortOrder: SortOrder): List<BookInfo> {
        return repository.sortBooks(sortOrder)
    }
}