package ru.mamykin.foboreader.book_details.presentation

class BookDetailsFeatureTest {

    // private val bookId: Long = 100L
    // private val getBookDetails: GetBookDetails = mockk()
    // private val actor = BookDetailsViewModel.BookDetailsActor(
    //     bookId = bookId,
    //     getBookDetails = getBookDetails,
    // )
    // private val reducer = BookDetailsViewModel.BookDetailsReducer()
    // private val testScope = TestScope()
    // private val feature = BookDetailsViewModel(actor, reducer, testScope)
    //
    // @Test
    // fun `open book`() = runTest {
    //     every { screenProvider.readBookScreen(bookId) } returns readBookScreen
    //     val prevState = feature.state
    //
    //     feature.sendIntent(BookDetailsViewModel.Intent.OpenBook)
    //     testScope.advanceUntilIdle()
    //
    //     assertEquals(prevState, feature.state)
    //     assertEquals(BookDetailsViewModel.Effect.NavigateToReadBook(bookId), feature.effectFlow.first())
    // }
    //
    // @Test
    // fun `load book info`() = runTest {
    //     val bookDetails: BookDetails = mockk()
    //     coEvery { getBookDetails.execute(bookId) } returns bookDetails
    //
    //     feature.sendIntent(BookDetailsViewModel.Intent.LoadBookInfo)
    //     testScope.advanceUntilIdle()
    //
    //     assertEquals(BookDetailsViewModel.State.Loaded(bookDetails), feature.state)
    // }
}