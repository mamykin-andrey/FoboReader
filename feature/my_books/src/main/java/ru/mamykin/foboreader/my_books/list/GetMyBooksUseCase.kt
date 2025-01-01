package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import javax.inject.Inject

internal class GetMyBooksUseCase @Inject constructor(
    private val myBooksRepository: MyBooksRepository,
    private val booksScanner: BookFilesScanner,
) {
    suspend fun execute(): Result<List<BookInfo>> {
        return booksScanner.scan().map { allStoredBooks ->
            myBooksRepository.updateBooks(allStoredBooks)
            myBooksRepository.getBooks()
        }
    }
}