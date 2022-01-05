package ru.mamykin.foboreader.book_details.domain.usecase

import ru.mamykin.foboreader.book_details.domain.model.BookDetails
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import javax.inject.Inject

class GetBookDetails @Inject constructor(
    private val repository: BookInfoRepository,
) {
    suspend fun execute(param: Long): BookDetails {
        val info = repository.getBookInfo(param)
        return BookDetails(
            author = info.author,
            title = info.title,
            coverUrl = info.coverUrl,
            filePath = info.filePath,
            currentPage = info.currentPage,
            genre = info.genre
        )
    }
}