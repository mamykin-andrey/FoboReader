package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.domain.usecase.base.SuspendUseCase
import ru.mamykin.foboreader.store.data.network.DownloadFileException
import ru.mamykin.foboreader.store.data.network.FileDownloader
import ru.mamykin.foboreader.store.domain.model.StoreBook

class DownloadBook(
    private val fileDownloader: FileDownloader
) : SuspendUseCase<StoreBook, Unit>() {

    @Throws(DownloadFileException::class)
    override suspend fun execute(param: StoreBook) {
        fileDownloader.download(param.link, param.getFileName())
    }
}