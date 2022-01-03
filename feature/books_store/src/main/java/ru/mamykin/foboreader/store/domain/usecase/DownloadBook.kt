package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.Result
import ru.mamykin.foboreader.store.data.network.FileDownloader
import ru.mamykin.foboreader.store.domain.model.StoreBook
import javax.inject.Inject

class DownloadBook @Inject constructor(
    private val fileDownloader: FileDownloader
) {
    suspend fun execute(book: StoreBook): Result<Unit> {
        return runCatching {
            Result.success(fileDownloader.download(book.link, book.getFileName()))
        }.getOrElse { Result.error(it) }
    }
}