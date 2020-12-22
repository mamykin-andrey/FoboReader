package ru.mamykin.foboreader.my_books.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.my_books.data.MyBooksRepository

@FlowPreview
@ExperimentalCoroutinesApi
class GetMyBooks(
    private val repository: MyBooksRepository
) {
    operator fun invoke(): Flow<List<BookInfo>> {
        return repository.booksFlow
    }
}