package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

internal class GetMyBooks @Inject constructor(
    private val repository: MyBooksRepository,
) {
    suspend fun execute(force: Boolean): List<BookInfo> {
        return repository.getBooks(force)
    }
}