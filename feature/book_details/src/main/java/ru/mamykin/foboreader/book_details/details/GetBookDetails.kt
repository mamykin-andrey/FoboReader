package ru.mamykin.foboreader.book_details.details

import ru.mamykin.foboreader.book_details.details.BookDetails
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import javax.inject.Inject

class GetBookDetails @Inject constructor(
    private val repository: BookInfoRepository,
) {
    suspend fun execute(bookId: Long): BookDetails {
        val info = repository.getBookInfo(bookId)
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