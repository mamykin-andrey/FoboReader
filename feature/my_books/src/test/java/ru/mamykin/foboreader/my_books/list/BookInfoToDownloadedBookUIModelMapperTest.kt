package ru.mamykin.foboreader.my_books.list

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Test
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBook
import ru.mamykin.foboreader.core.presentation.StringOrResource
import ru.mamykin.foboreader.my_books.R
import java.util.Date

class BookInfoToDownloadedBookUIModelMapperTest {

    private val mapper = BookInfoUIModelMapper()

    @Test
    fun `mapping from domain model`() {
        val domainModel = createDomainModel()

        val uiModel = mapper.map(domainModel)

        assertTrue(
            uiModel.id == domainModel.id &&
                uiModel.genre == domainModel.genre &&
                uiModel.coverUrl == domainModel.coverUrl &&
                uiModel.author == domainModel.author &&
                uiModel.title == domainModel.title &&
                uiModel.languages == domainModel.languages &&
                uiModel.lastOpen == domainModel.lastOpen
        )
    }

    @Test
    fun `read status is default when book wasn't opened`() {
        val domainModel = createDomainModel(totalPages = null)

        val uiModel = mapper.map(domainModel)

        assertEquals(
            StringOrResource.Resource(R.string.book_pages_info_not_started),
            uiModel.readStatus
        )
    }

    @Test
    fun `read status is correct when book was opened`() {
        val domainModel = createDomainModel()

        val uiModel = mapper.map(domainModel)

        assertEquals(
            StringOrResource.Resource(R.string.book_pages_info, 1, 10),
            uiModel.readStatus
        )
    }

    private fun createDomainModel(totalPages: Int? = 10): DownloadedBook {
        return DownloadedBook(
            id = 100L,
            filePath = "",
            genre = "",
            coverUrl = "",
            author = "",
            title = "",
            languages = listOf("1", "2"),
            date = Date(),
            currentPage = 0,
            totalPages = totalPages,
            lastOpen = Date().time,
        )
    }
}