package ru.mamykin.foboreader.store.list

import ru.mamykin.foboreader.store.list.DownloadFileException
import java.io.File

internal interface FileRepository {

    @Throws(DownloadFileException::class)
    fun createFile(fileName: String): File

    @Throws(DownloadFileException::class)
    suspend fun downloadFile(url: String, outputFile: File)
}