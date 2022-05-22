package ru.mamykin.foboreader.store.domain.usecase

import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.store.data.network.FileRepository
import javax.inject.Inject

internal class DownloadBook @Inject constructor(
    private val fileRepository: FileRepository,
) {
    suspend fun execute(bookLink: String, outputFileName: String): Result<Unit> = runCatching {
        val newFile = fileRepository.createFile(outputFileName)
        fileRepository.downloadFile(bookLink, newFile)
    }.onSuccess {
        Log.debug("File downloaded to $outputFileName")
    }
}