package ru.mamykin.foboreader.store.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import ru.mamykin.foboreader.store.data.network.DownloadFileException
import ru.mamykin.foboreader.store.data.network.FileDownloader
import ru.mamykin.foboreader.store.domain.model.StoreBook

@ExperimentalCoroutinesApi
@FlowPreview
class DownloadBook(
    private val fileDownloader: FileDownloader
) {
    @Throws(DownloadFileException::class)
    suspend fun execute(book: StoreBook) {
        fileDownloader.download(book.link, book.getFileName())
    }
}