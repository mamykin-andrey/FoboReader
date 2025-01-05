package ru.mamykin.foboreader.common_book_info.domain

import ru.mamykin.foboreader.common_book_info.data.repository.DownloadedBooksRepository
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import javax.inject.Inject

class GetBookInfoUseCase @Inject constructor(
    private val bookInfoRepository: DownloadedBooksRepository,
) {
    suspend fun execute(bookId: Long): DownloadedBookEntity {
        return bookInfoRepository.getBookInfo(bookId)
    }
}