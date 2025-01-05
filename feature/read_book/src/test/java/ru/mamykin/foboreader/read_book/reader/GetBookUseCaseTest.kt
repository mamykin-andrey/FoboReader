package ru.mamykin.foboreader.read_book.reader

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import ru.mamykin.foboreader.common_book_info.data.repository.DownloadedBooksRepository
import ru.mamykin.foboreader.core.data.AppSettingsRepository

class GetBookUseCaseTest {

    private val bookContentRepository: BookContentRepository = mockk()
    private val bookInfoRepository: DownloadedBooksRepository = mockk()
    private val appSettingsRepository: AppSettingsRepository = mockk {
        every { getReadTextSize() } returns 15
    }
    private val getBookUseCase = GetBookUseCase(bookContentRepository, bookInfoRepository, appSettingsRepository)
    private val bookId = 100L
    private val bookFilePath = "data/data/test_file.txt"

    @Test
    fun `split text when it's one page`() = runTest {
        coEvery { bookInfoRepository.getBookInfo(bookId) } returns mockk {
            coEvery { filePath } returns bookFilePath
        }
        coEvery { bookContentRepository.getBookContent(bookFilePath) } returns BookContent(
            sentences = listOf("Original sentence#1"), // total = 20 with line breaks
            translations = emptyList(),
        )
        val measurer = LengthTextMeasurer(100)

        val content = getBookUseCase.execute(bookId, measurer, 0 to 0)

        assertEquals(
            listOf("Original sentence#1"),
            content.pages
        )
    }

    @Test
    fun `split text when it's two pages`() = runTest {
        coEvery { bookInfoRepository.getBookInfo(bookId) } returns mockk {
            coEvery { filePath } returns bookFilePath
        }
        coEvery { bookContentRepository.getBookContent(bookFilePath) } returns BookContent(
            sentences = listOf("Original sentence#1", "Original sentence#2"), // total = 40 with line breaks
            translations = emptyList(),
        )
        val measurer = LengthTextMeasurer(30)

        val content = getBookUseCase.execute(bookId, measurer, 0 to 0)

        assertEquals(
            listOf(
                "Original sentence#1",
                "Original sentence#2",
            ),
            content.pages
        )
    }

    @Test
    fun `split text when it's many pages`() = runTest {
        coEvery { bookInfoRepository.getBookInfo(bookId) } returns mockk {
            coEvery { filePath } returns bookFilePath
        }
        coEvery { bookContentRepository.getBookContent(bookFilePath) } returns BookContent(
            sentences = listOf(
                "Original sentence#1",
                "Original sentence#2",
                "Original sentence#3",
                "Original sentence#4",
                "Original sentence#5",
            ), // total = 100 with line breaks
            translations = emptyList(), // ignored for now
        )
        val measurer = LengthTextMeasurer(50)

        val content = getBookUseCase.execute(bookId, measurer, 0 to 0)

        assertEquals(
            listOf(
                "Original sentence#1\nOriginal sentence#2",
                "Original sentence#3\nOriginal sentence#4",
                "Original sentence#5",
            ),
            content.pages
        )
    }

    private class LengthTextMeasurer(
        private val maxLength: Int,
    ) : TextMeasurer {

        override fun isTextFit(text: String, screenSize: Pair<Int, Int>, fontSize: Int): Boolean {
            return text.length <= maxLength
        }
    }
}