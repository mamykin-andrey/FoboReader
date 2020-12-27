package ru.mamykin.foboreader.my_books.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.domain.FlowUseCase
import ru.mamykin.foboreader.my_books.data.MyBooksRepository

class GetMyBooks(
    private val repository: MyBooksRepository
) : FlowUseCase<Unit, List<BookInfo>>() {

    override fun execute(): Flow<List<BookInfo>> {
        return repository.getBooks()
    }
}