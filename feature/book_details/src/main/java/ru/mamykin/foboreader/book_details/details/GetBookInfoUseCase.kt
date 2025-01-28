package ru.mamykin.foboreader.book_details.details

import ru.mamykin.foboreader.book_details.MockBookInfoService
import ru.mamykin.foboreader.common_book_info.data.repository.DownloadedBooksRepository
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import javax.inject.Inject

internal class GetBookInfoUseCase @Inject constructor(
    private val bookInfoRepository: DownloadedBooksRepository,
    private val mockBookInfoService: MockBookInfoService,
) {
    suspend fun execute(bookId: Long): DownloadedBookEntity {
        return bookInfoRepository.getBookInfo(bookId)
            .let {
                val userRating = mockBookInfoService.getBookRating(bookId)
                it.copy(isRatedByUser = userRating != null)
            }
    }
}