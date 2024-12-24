package ru.mamykin.foboreader.store.list

import javax.inject.Inject

internal class DownloadBook @Inject constructor(
    private val fileRepository: FileRepository,
) {
    suspend fun execute(bookLink: String, outputFileName: String): Result<Unit> {
        return fileRepository.createFile(outputFileName).map {
            fileRepository.downloadFile(bookLink, it)
        }
    }
}