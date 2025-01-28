package ru.mamykin.foboreader.book_details.rate

import ru.mamykin.foboreader.book_details.BookInfoRepository
import javax.inject.Inject

internal class RateBookUseCase @Inject constructor(
    private val bookInfoRepository: BookInfoRepository,
) {
    suspend fun execute(bookId: Long, rating: Int): Result<Unit> {
        return bookInfoRepository.rateBook(bookId, rating)
    }
}