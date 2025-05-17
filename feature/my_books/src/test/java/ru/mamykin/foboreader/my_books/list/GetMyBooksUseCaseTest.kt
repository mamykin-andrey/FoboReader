package ru.mamykin.foboreader.my_books.list

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBook

class GetMyBooksUseCaseTest {

    private val myBooksRepository: MyBooksRepository = mockk()
    private val booksScanner: BookFilesScanner = mockk()
    private val loadMyBooks = GetMyBooksUseCase(myBooksRepository, booksScanner)
    private val testBooks = listOf(
        DownloadedBook(0L, "", "", null, "", "", emptyList(), null, 0, null, 0L),
        DownloadedBook(1L, "", "", null, "", "", emptyList(), null, 0, null, 0L),
    )

    @Test
    fun `execute returns and updates scanned books`() = runTest {
        coEvery { booksScanner.scan() } returns Result.success(testBooks)
        coEvery { myBooksRepository.updateBooks(any()) } just Runs

        val books = loadMyBooks.execute()

        assertEquals(testBooks, books)
        coVerify { myBooksRepository.updateBooks(testBooks) }
    }
}