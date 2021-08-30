package ru.mamykin.foboreader.store.presentation

import ru.mamykin.foboreader.core.presentation.Feature
import javax.inject.Inject

class BooksStoreFeature @Inject constructor(
    reducer: BooksStoreReducer,
    actor: BooksStoreActor
) : Feature<BooksStore.ViewState, BooksStore.Intent, BooksStore.Effect, BooksStore.Action, Nothing>(
    BooksStore.ViewState(),
    actor,
    reducer
) {
    private val uiEventTransformer = UiEventTransformer()

    init {
        sendIntent(BooksStore.Intent.LoadBooks)
    }

    fun sendEvent(event: BooksStore.Event) {
        sendIntent(uiEventTransformer.invoke(event))
    }

    private class UiEventTransformer {

        operator fun invoke(event: BooksStore.Event): BooksStore.Intent = when (event) {
            is BooksStore.Event.FilterQueryChanged -> {
                BooksStore.Intent.FilterBooks(event.query)
            }
            is BooksStore.Event.DownloadBookClicked -> {
                BooksStore.Intent.DownloadBook(event.book)
            }
            is BooksStore.Event.RetryBooksClicked -> {
                BooksStore.Intent.LoadBooks
            }
        }
    }
}