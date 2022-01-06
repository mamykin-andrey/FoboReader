package ru.mamykin.foboreader.book_details.presentation

import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import io.mockk.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test
import ru.mamykin.foboreader.book_details.domain.model.BookDetails
import ru.mamykin.foboreader.book_details.domain.usecase.GetBookDetails
import ru.mamykin.foboreader.core.navigation.ScreenProvider

class BookDetailsActorTest {

    private val bookId: Long = 0L
    private val router: Router = mockk()
    private val screenProvider: ScreenProvider = mockk()
    private val getBookDetails: GetBookDetails = mockk()
    private val screen: Screen = mockk()
    private val actor = BookDetailsFeature.BookDetailsActor(
        bookId,
        router,
        screenProvider,
        getBookDetails,
    )

    @Test
    fun `test open book`() {
        runBlocking {
            every { screenProvider.readBookScreen(bookId) } returns screen
            every { router.navigateTo(screen) } just Runs

            actor.invoke(BookDetailsFeature.Intent.OpenBook).collect()

            verify { router.navigateTo(screen) }
        }
    }

    @Test
    fun `test load book`() {
        runBlocking {
            val bookDetails: BookDetails = mockk()
            coEvery { getBookDetails.execute(bookId) } returns bookDetails

            val actions = actor.invoke(BookDetailsFeature.Intent.LoadBookInfo).toList()

            assert(actions.contains(BookDetailsFeature.Action.BookLoaded(bookDetails)))
        }
    }
}