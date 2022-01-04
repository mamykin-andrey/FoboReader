package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

internal class FilterMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    suspend fun execute(searchQuery: String): List<BookInfo> {
        return repository.filterBooks(searchQuery)
    }
}