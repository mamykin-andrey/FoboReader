package ru.mamykin.foboreader.read_book.domain.usecase

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.mamykin.foboreader.read_book.reader.BookContentRepository
import ru.mamykin.foboreader.read_book.reader.GetBookText

class GetBookTextTest {

    private val bookContentRepository: BookContentRepository = mockk()
    private val getBookText = GetBookText(bookContentRepository)

    @Test(expected = IllegalStateException::class)
    fun execute_whenRepositoryThrowsException_throwException() {
        runBlocking {
            val expectedException = IllegalStateException("")
            coEvery { bookContentRepository.getBookContent(any()) } throws expectedException

            getBookText.execute("", mockk(), 0 to 0)
        }
    }
}