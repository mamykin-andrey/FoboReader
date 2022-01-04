package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import ru.mamykin.foboreader.my_books.domain.model.SortOrder
import javax.inject.Inject

internal class SortMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    suspend fun execute(param: SortOrder): List<BookInfo> {
        return repository.sortBooks(param)
    }
}