package ru.mamykin.foboreader.book_details.presentation

import com.github.terrakok.cicerone.Screen
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import ru.mamykin.foboreader.book_details.details.BookDetails
import ru.mamykin.foboreader.book_details.details.BookDetailsFeature
import ru.mamykin.foboreader.book_details.details.GetBookDetails
import ru.mamykin.foboreader.core.navigation.ScreenProvider

class BookDetailsFeatureTest {

    private val bookId: Long = 100L
    private val screenProvider: ScreenProvider = mockk()
    private val getBookDetails: GetBookDetails = mockk()
    private val actor = BookDetailsFeature.BookDetailsActor(
        bookId = bookId,
        getBookDetails = getBookDetails,
    )
    private val reducer = BookDetailsFeature.BookDetailsReducer()
    private val testScope = TestScope()
    private val feature = BookDetailsFeature(actor, reducer, testScope)

    @Test
    fun `open book`() = runTest {
        val readBookScreen: Screen = mockk()
        every { screenProvider.readBookScreen(bookId) } returns readBookScreen
        val prevState = feature.state

        feature.sendIntent(BookDetailsFeature.Intent.OpenBook)
        testScope.advanceUntilIdle()

        assertEquals(prevState, feature.state)
        assertEquals(BookDetailsFeature.Effect.NavigateToReadBook(bookId), feature.effectFlow.first())
    }

    @Test
    fun `load book info`() = runTest {
        val bookDetails: BookDetails = mockk()
        coEvery { getBookDetails.execute(bookId) } returns bookDetails

        feature.sendIntent(BookDetailsFeature.Intent.LoadBookInfo)
        testScope.advanceUntilIdle()

        assertEquals(BookDetailsFeature.State.Loaded(bookDetails), feature.state)
    }
}