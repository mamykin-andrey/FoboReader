package ru.mamykin.foboreader.domain.devicebooks

import ru.mamykin.foboreader.core.extension.isFictionBook
import ru.mamykin.foboreader.data.model.FileStructure
import ru.mamykin.foboreader.data.repository.devicebooks.DeviceBooksRepository
import rx.Single
import java.io.File
import javax.inject.Inject

class DeviceBooksInteractor @Inject constructor(
        private val repository: DeviceBooksRepository
) {
    private var currentDir: String = ""

    fun getDirectoryFiles(directory: String): Single<List<FileStructure>> {
        currentDir = directory

        return repository.getFiles(currentDir)
                .map { it.sortedBy(this::getFileWeight) }
    }

    fun getRootDirectoryFiles(): Single<FileStructure> {
        currentDir = repository.getRootDirectory()
        return getDirectoryFiles(currentDir).map { it.first() }
    }

    fun getParentDirectoryFiles(): Single<List<FileStructure>> {
        currentDir = getParentDirectory()
        return getDirectoryFiles(currentDir)
    }

    fun openFile(file: File): Single<String> = when {
        !file.canRead() -> Single.error(AccessDeniedException())
        !file.isFictionBook -> Single.error(UnknownBookFormatException())
        else -> Single.just(file.absolutePath)
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