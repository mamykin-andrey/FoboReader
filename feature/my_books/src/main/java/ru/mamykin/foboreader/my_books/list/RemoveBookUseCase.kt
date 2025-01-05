package ru.mamykin.foboreader.my_books.list

import ru.mamykin.foboreader.common_book_info.data.repository.DownloadedBooksRepository
import javax.inject.Inject

internal class RemoveBookUseCase @Inject constructor(
    private val repository: DownloadedBooksRepository
) {
    suspend fun execute(bookId: Long): Result<Boolean> {
        return repository.removeBook(bookId)
    }
}