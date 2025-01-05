package ru.mamykin.foboreader.read_book.reader

import ru.mamykin.foboreader.common_book_info.data.repository.DownloadedBooksRepository
import javax.inject.Inject

class UpdateBookInfoUseCase @Inject constructor(
    private val bookInfoRepository: DownloadedBooksRepository
) {
    suspend fun execute(
        bookId: Long,
        currentPage: Int,
        totalPages: Int,
    ) {
        bookInfoRepository.updateBookInfo(
            bookId,
            currentPage,
            totalPages
        )
    }
}