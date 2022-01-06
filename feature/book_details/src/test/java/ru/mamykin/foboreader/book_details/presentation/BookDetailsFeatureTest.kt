package ru.mamykin.foboreader.book_details.presentation

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.mamykin.foboreader.book_details.domain.model.BookDetails
import ru.mamykin.foboreader.core.platform.ResourceManager

class BookDetailsFeatureTest {

    private val testStr = "test"
    private val resourceManager: ResourceManager = mockk()
    private val reducer = BookDetailsFeature.BookDetailsReducer(resourceManager)

    init {
        every { resourceManager.getString(any()) } returns testStr
    }

    @Test
    fun `test loading book finished`() {
        runBlocking {
            val state = BookDetailsFeature.State()
            val bookDetails = BookDetails(
                author = "",
                title = "",
                coverUrl = null,
                filePath = "",
                currentPage = 0,
                genre = "",
            )

            val (newState, _) = reducer.invoke(state, BookDetailsFeature.Action.BookLoaded(bookDetails))

            assert(!newState.isError)
            assert(!newState.isLoading)
            assert(newState.bookDetails == bookDetails)
            assert(!newState.items.isNullOrEmpty())
        }
    }

    @Test
    fun `test loading book failed`() {
        runBlocking {
            val state = BookDetailsFeature.State()

            val (newState, _) = reducer.invoke(state, BookDetailsFeature.Action.LoadingError)

            assert(newState.isError)
            assert(!newState.isLoading)
            assert(newState.bookDetails == null)
            assert(newState.items.isNullOrEmpty())
        }
    }
}