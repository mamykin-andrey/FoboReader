package ru.mamykin.store.domain

import ru.mamykin.store.R
import ru.mamykin.store.data.BooksStoreRepository
import ru.mamykin.store.domain.model.StoreBook
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BooksStoreInteractor constructor(
        private val repository: BooksStoreRepository,
        private val fileDownloader: FileDownloader
) {
    suspend fun getBooks(): List<StoreBook> {
        return repository.getBooks()
    }

    suspend fun downloadBook(book: StoreBook): Unit = suspendCoroutine { cont ->
        fileDownloader.download(
                url = book.link,
                fileName = book.getBookName(),
                title = book.title,
                descriptionRes = R.string.downloading_book,
                onSuccess = { cont.resumeWith(Result.success(Unit)) },
                onError = { cont.resumeWithException(DownloadBookException(it)) }
        )
    }

    fun onCleared() {
        fileDownloader.release()
    }
}