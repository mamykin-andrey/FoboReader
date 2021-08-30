package ru.mamykin.foboreader.store.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.mamykin.foboreader.core.presentation.Actor
import ru.mamykin.foboreader.store.domain.usecase.DownloadBook
import ru.mamykin.foboreader.store.domain.usecase.FilterStoreBooks
import ru.mamykin.foboreader.store.domain.usecase.GetStoreBooks
import javax.inject.Inject

class BooksStoreActor @Inject constructor(
    private val downloadStoreBook: DownloadBook,
    private val getStoreBooks: GetStoreBooks,
    private val filterStoreBooks: FilterStoreBooks
) : Actor<BooksStore.Intent, BooksStore.Action> {

    override operator fun invoke(intent: BooksStore.Intent): Flow<BooksStore.Action> = flow {
        when (intent) {
            is BooksStore.Intent.LoadBooks -> {
                emit(BooksStore.Action.BooksLoading)
                getStoreBooks(Unit).catchMap(
                    { emit(BooksStore.Action.BooksLoaded(it)) },
                    { emit(BooksStore.Action.BooksLoadingError(it.message.orEmpty())) }
                )
            }
            is BooksStore.Intent.FilterBooks -> {
                emit(
                    BooksStore.Action.BooksLoaded(
                        filterStoreBooks(intent.query).getOrThrow()
                    )
                )
            }
            is BooksStore.Intent.DownloadBook -> {
                emit(BooksStore.Action.DownloadBookStarted)
                downloadStoreBook(intent.book).catchMap(
                    { emit(BooksStore.Action.BookDownloaded) },
                    { emit(BooksStore.Action.BookDownloadError(it.message.orEmpty())) }
                )
            }
        }
    }
}