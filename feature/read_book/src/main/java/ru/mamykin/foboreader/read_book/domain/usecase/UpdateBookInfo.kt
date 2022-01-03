package ru.mamykin.foboreader.read_book.domain.usecase

import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.core.domain.usecase.base.Result
import javax.inject.Inject

class UpdateBookInfo @Inject constructor(
    private val bookInfoRepository: BookInfoRepository
) {
    suspend fun execute(
        bookId: Long,
        currentPage: Int,
        totalPages: Int,
    ): Result<Unit> {
        return runCatching {
            Result.success(
                bookInfoRepository.updateBookInfo(
                    bookId,
                    currentPage,
                    totalPages
                )
            )
        }.getOrElse { Result.error(it) }
    }
}