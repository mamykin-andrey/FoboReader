package ru.mamykin.foboreader.book_details

import javax.inject.Inject

internal class BookInfoRepository @Inject constructor(
    private val service: MockBookInfoService,
) {
    suspend fun rateBook(bookId: Long, rating: Int): Result<Unit> {
        return service.rateBook(bookId, rating)
    }
}