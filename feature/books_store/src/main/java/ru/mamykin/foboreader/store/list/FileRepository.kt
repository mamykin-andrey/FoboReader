package ru.mamykin.foboreader.store.list

import java.io.File

internal interface FileRepository {

    fun createFile(fileName: String): Result<File>

    suspend fun downloadFile(url: String, outputFile: File): Result<Unit>
}