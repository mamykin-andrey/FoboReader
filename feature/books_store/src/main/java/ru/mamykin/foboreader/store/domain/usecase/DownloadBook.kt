package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.store.data.network.FileDownloader
import ru.mamykin.foboreader.store.domain.model.StoreBook
import javax.inject.Inject

class DownloadBook @Inject constructor(
    private val fileDownloader: FileDownloader
) {
    suspend fun execute(bookLink: String, fileName: String): Result<Unit> {
        return runCatching {
            fileDownloader.download(bookLink, fileName)
        }
    }
}