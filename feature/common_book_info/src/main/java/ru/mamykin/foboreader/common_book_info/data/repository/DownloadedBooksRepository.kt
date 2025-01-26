package ru.mamykin.foboreader.common_book_info.data.repository

import ru.mamykin.foboreader.common_book_info.data.database.DownloadedBooksDao
import ru.mamykin.foboreader.common_book_info.data.model.DownloadedBookDBModel
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import java.io.File
import java.util.Date
import javax.inject.Inject

// TODO: Add flows for reactive updating
class DownloadedBooksRepository @Inject constructor(
    private val downloadedBooksDao: DownloadedBooksDao,
) {
    suspend fun getBooks(): List<DownloadedBookEntity> {
        return downloadedBooksDao.getBooks().map { it.toDomainModel() }
    }

    suspend fun getBookInfo(id: Long): DownloadedBookEntity {
        return downloadedBooksDao.getBook(id).toDomainModel()
    }

    suspend fun removeBook(bookId: Long) = runCatching {
        val info = getBookInfo(bookId)
        downloadedBooksDao.remove(bookId)
        File(info.filePath).delete()
    }

    suspend fun updateBookInfo(bookId: Long, currentPage: Int, totalPages: Int) {
        downloadedBooksDao.update(bookId, currentPage, totalPages)
    }

    suspend fun saveBook(
        filePath: String,
        genre: String,
        coverUrl: String?,
        author: String,
        title: String,
        languages: List<String>,
        link: String,
        rating: Float,
        isRatedByUser: Boolean,
    ) {
        downloadedBooksDao.insert(
            DownloadedBookDBModel(
                id = 0,
                filePath = filePath,
                genre = genre,
                coverUrl = coverUrl,
                author = author,
                title = title,
                languages = languages,
                date = Date(),
                currentPage = 0,
                totalPages = null,
                lastOpen = null,
                link = link,
                rating = rating,
                isRatedByUser = isRatedByUser,
            )
        )
    }
}