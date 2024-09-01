package ru.mamykin.foboreader.store.list

import ru.mamykin.foboreader.core.platform.Log
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