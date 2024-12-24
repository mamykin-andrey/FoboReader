package ru.mamykin.foboreader.my_books.list

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo

class LoadMyBooksTest {

    private val myBooksRepository: MyBooksRepository = mockk()
    private val booksScanner: BookFilesScanner = mockk()
    private val loadMyBooks = LoadMyBooks(myBooksRepository, booksScanner)
    private val testBooks = listOf(
        BookInfo(0L, "", "", null, "", "", emptyList(), null, 0, null, 0L),
        BookInfo(1L, "", "", null, "", "", emptyList(), null, 0, null, 0L),
    )

    @Test
    fun `execute returns and updates scanned books`() = runTest {
        coEvery { booksScanner.scan() } returns testBooks
        coEvery { myBooksRepository.updateBooks(any()) } just Runs

        val books = loadMyBooks.execute()

        assertEquals(testBooks, books)
        coVerify { myBooksRepository.updateBooks(testBooks) }
    }
}