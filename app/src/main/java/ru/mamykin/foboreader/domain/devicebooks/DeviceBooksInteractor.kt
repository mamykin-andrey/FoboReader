package ru.mamykin.foboreader.domain.devicebooks

import ru.mamykin.foboreader.core.extension.isFictionBook
import ru.mamykin.foboreader.data.repository.devicebooks.DeviceBooksRepository
import rx.Single
import java.io.File
import javax.inject.Inject

class DeviceBooksInteractor @Inject constructor(
        private val repository: DeviceBooksRepository
) {
    private var currentDir: String = ""

    fun getDirectoryFiles(directory: String): Single<FileStructureEntity> {
        currentDir = directory

        return repository.getFiles(currentDir)
                .map { it.sortedBy(this::getFileWeight) }
                .zipWith(repository.canReadDirectory(getParentDirectory())) { f, c -> Pair(f, c) }
                .map { FileStructureEntity(currentDir, it.first, it.second) }
    }

    fun getRootDirectoryFiles(): Single<FileStructureEntity> {
        currentDir = repository.getRootDirectory()
        return getDirectoryFiles(currentDir)
    }

    fun getParentDirectoryFiles(): Single<FileStructureEntity> {
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

    private fun getFileWeight(file: File) = when {
        file.isDirectory -> 0
        file.isFictionBook -> 1
        else -> 2
    }
}