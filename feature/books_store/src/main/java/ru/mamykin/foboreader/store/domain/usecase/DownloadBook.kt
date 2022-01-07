package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.platform.*
import ru.mamykin.foboreader.store.R
import ru.mamykin.foboreader.store.data.network.FileRepository
import javax.inject.Inject

internal class DownloadBook @Inject constructor(
    private val fileRepository: FileRepository,
    private val resourceManager: ResourceManager,
    private val notificationManager: NotificationManager,
) {
    private val downloadStarted by lazy { resourceManager.getString(R.string.books_store_download_progress) }
    private val downloadFinished by lazy { resourceManager.getString(R.string.books_store_download_completed) }
    private val downloadFailed by lazy { resourceManager.getString(R.string.books_store_download_failed) }

    suspend fun execute(bookLink: String, outputFileName: String): Result<Unit> {
        showNotification(outputFileName, downloadStarted)

        return runCatching {
            val newFile = fileRepository.createFile(outputFileName)
            fileRepository.downloadFile(bookLink, newFile)
        }.onSuccess {
            showNotification(outputFileName, downloadFinished)
            Log.debug("File downloaded to $outputFileName")
        }.onFailure {
            showNotification(outputFileName, downloadFailed)
            throw it
        }
    }

    private fun showNotification(fileName: String, status: String) {
        notificationManager.notify(
            iconRes = R.drawable.ic_download,
            title = status,
            text = fileName,
            channelId = ChannelId.GENERAL,
            notificationId = NotificationId.FILE_DOWNLOAD,
        )
    }
}