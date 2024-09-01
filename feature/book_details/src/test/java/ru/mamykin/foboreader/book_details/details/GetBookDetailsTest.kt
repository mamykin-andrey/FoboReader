package ru.mamykin.foboreader.book_details.details

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

class GetBookDetailsTest {

    private val repository: BookInfoRepository = mockk()
    private val getBookDetails = GetBookDetails(repository)

    @Test
    fun `test get details`() = runBlocking {
        val testBookInfo = BookInfo(0, "", "", "", "", "", listOf(), null, 0, 1, 0)
        coEvery { repository.getBooks() } returns listOf(testBookInfo)

        val bookDetails = getBookDetails.execute(0)

        assertTrue(
            bookDetails.author == testBookInfo.author
                && bookDetails.title == testBookInfo.title
                && bookDetails.coverUrl == testBookInfo.coverUrl
                && bookDetails.filePath == testBookInfo.filePath
                && bookDetails.currentPage == testBookInfo.currentPage
                && bookDetails.genre == testBookInfo.genre
        )
    }
}