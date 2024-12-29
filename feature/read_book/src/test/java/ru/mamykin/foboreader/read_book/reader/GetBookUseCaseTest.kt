package ru.mamykin.foboreader.read_book.reader

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.mamykin.foboreader.common_book_info.data.repository.BookInfoRepository

class GetBookUseCaseTest {

    private val bookContentRepository: BookContentRepository = mockk()
    private val bookInfoRepository: BookInfoRepository = mockk()
    private val getBookUseCase = GetBookUseCase(bookContentRepository, bookInfoRepository)
    private val bookId = 100L
    private val bookFilePath = "C://Program files/GTA3/game.exe"

    @Test
    fun `split text when it's one page`() = runTest {
        coEvery { bookInfoRepository.getBookInfo(bookId) } returns mockk {
            coEvery { filePath } returns bookFilePath
        }
        coEvery { bookContentRepository.getBookContent(bookFilePath) } returns TranslatedText(
            sentences = listOf("Original sentence #1"), // total = 20
            translations = listOf("Translated sentence #1"),
        )
        val measurer = LengthTextMeasurer(100)

        val content = getBookUseCase.execute(bookId, measurer, 0 to 0)

        assertEquals(
            listOf(
                TranslatedText(
                    sentences = listOf("Original sentence #1"),
                    translations = listOf("Translated sentence #1")
                )
            ),
            content.pages
        )
    }

    @Test
    fun `split text when it's two pages`() = runTest {
        coEvery { bookInfoRepository.getBookInfo(bookId) } returns mockk {
            coEvery { filePath } returns bookFilePath
        }
        coEvery { bookContentRepository.getBookContent(bookFilePath) } returns TranslatedText(
            sentences = listOf("Original sentence #1", "Original sentence #2"), // total = 40
            translations = listOf("Translated sentence #1", "Translated sentence #2"),
        )
        val measurer = LengthTextMeasurer(30)

        val content = getBookUseCase.execute(bookId, measurer, 0 to 0)

        assertEquals(
            listOf(
                TranslatedText(
                    sentences = listOf("Original sentence #1"),
                    translations = listOf("Translated sentence #1")
                ),
                TranslatedText(
                    sentences = listOf("Original sentence #2"),
                    translations = listOf("Translated sentence #2")
                ),
            ),
            content.pages
        )
    }

    private class LengthTextMeasurer(
        private val length: Int,
    ) : TextSplitter {

        override fun doesTheTextFit(text: String, screenSize: Pair<Int, Int>): Boolean {
            return text.length <= length
        }
    }
}