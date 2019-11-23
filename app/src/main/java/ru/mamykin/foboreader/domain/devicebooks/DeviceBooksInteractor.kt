package ru.mamykin.foboreader.domain.devicebooks

import ru.mamykin.core.extension.isFictionBook
import ru.mamykin.foboreader.data.model.FileStructure
import ru.mamykin.foboreader.data.repository.devicebooks.DeviceBooksRepository
import java.io.File
import javax.inject.Inject

class DeviceBooksInteractor @Inject constructor(
        private val repository: DeviceBooksRepository
) {
    private var currentDir: String = ""

    fun getDirectoryFiles(directory: String): List<FileStructure> {
        currentDir = directory
        return repository.getFiles(currentDir).sortedBy(this::getFileWeight)
    }

    fun getRootDirectoryFiles(): FileStructure {
        currentDir = repository.getRootDirectory()
        return getDirectoryFiles(currentDir).first()
    }

    fun getParentDirectoryFiles(): List<FileStructure> {
        currentDir = getParentDirectory()
        return getDirectoryFiles(currentDir)
    }

    fun openFile(file: File): String = when {
        !file.canRead() -> throw AccessDeniedException()
        !file.isFictionBook -> throw UnknownBookFormatException()
        else -> file.absolutePath
    }

    private fun getParentDirectory(): String {
        return currentDir.substring(0, currentDir.lastIndexOf("/"))
    }

    private fun getFileWeight(file: FileStructure) = when (file.type) {
        FileStructure.FileType.Folder -> 0
        FileStructure.FileType.FictionBook -> 1
        else -> 2
    }
}