package ru.mamykin.foboreader.my_books.domain.usecase

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

class GetMyBooks @Inject constructor(
    private val repository: MyBooksRepository,
) {
    suspend fun execute(): List<BookInfo> {
        return repository.newGetBooks()
    }
}