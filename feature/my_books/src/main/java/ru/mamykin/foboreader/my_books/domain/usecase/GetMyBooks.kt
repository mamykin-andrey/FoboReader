package ru.mamykin.foboreader.my_books.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.data.MyBooksRepository
import javax.inject.Inject

class GetMyBooks @Inject constructor(
    private val repository: MyBooksRepository
) {
    fun execute(): Flow<List<BookInfo>> {
        return repository.getBooks()
    }
}