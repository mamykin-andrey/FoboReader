package ru.mamykin.foboreader.domain.devicebooks

import ru.mamykin.foboreader.data.repository.devicebooks.DeviceBooksRepository
import ru.mamykin.foboreader.extension.getWeight
import ru.mamykin.foboreader.extension.isFictionBook
import rx.Single
import java.io.File
import javax.inject.Inject

class DeviceBooksInteractor @Inject constructor(
        private val repository: DeviceBooksRepository
) {
    private var currentDir: String = ""

    fun getDirectoryFiles(directory: String): Single<FileStructureEntity> {
        this.currentDir = directory
        val parentDirectory = formatParentDirectory(currentDir)

        return repository.getFiles(currentDir)
                .map { it.sortedBy(File::getWeight) }
                .zipWith(repository.canReadDirectory(parentDirectory)) { f, c -> Pair(f, c) }
                .map { FileStructureEntity(it.first, it.second, currentDir) }
    }

    fun getRootDirectoryFiles(): Single<FileStructureEntity> {
        currentDir = repository.getRootDirectory()
        return getDirectoryFiles(currentDir)
    }

    fun getParentDirectoryFiles(): Single<FileStructureEntity> {
        this.currentDir = formatParentDirectory(currentDir)
        return getDirectoryFiles(currentDir)
    }

    fun openFile(file: File): Single<String> = when {
        !file.canRead() -> Single.error(AccessDeniedException())
        !file.isFictionBook -> Single.error(UnknownBookFormatException())
        else -> Single.just(file.absolutePath)
    }

    private fun formatParentDirectory(dir: String): String {
        return dir.substring(0, currentDir.lastIndexOf("/"))
    }
}