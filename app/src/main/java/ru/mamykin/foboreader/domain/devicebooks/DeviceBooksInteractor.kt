package ru.mamykin.foboreader.domain.devicebooks

import ru.mamykin.foboreader.data.repository.devicebooks.DeviceBooksRepository
import ru.mamykin.foboreader.entity.AndroidFile
import ru.mamykin.foboreader.extension.getWeight
import rx.Single
import javax.inject.Inject

class DeviceBooksInteractor @Inject constructor(
        private val repository: DeviceBooksRepository
) {
    private var currentDirectory: String = ""

    fun openDirectory(directory: String): Single<FileStructureEntity> {
        this.currentDirectory = directory
        val parentDirectory = formatParentDirectory(currentDirectory)

        return repository.getFiles(currentDirectory)
                .map { it.sortedBy(AndroidFile::getWeight) }
                .zipWith(repository.canReadDirectory(parentDirectory), { f, c -> Pair(f, c) })
                .map { FileStructureEntity(it.first, it.second, currentDirectory) }
    }

    fun openRootDirectory(): Single<FileStructureEntity> {
        this.currentDirectory = repository.getRootDirectory()
        return openDirectory(currentDirectory)
    }

    fun openParentDirectory(): Single<FileStructureEntity> {
        this.currentDirectory = formatParentDirectory(currentDirectory)
        return openDirectory(currentDirectory)
    }

    fun openFile(file: AndroidFile): Single<String> = when {
        !file.canRead() -> Single.error(AccessDeniedException())
        !file.isFictionBook -> Single.error(UnknownBookFormatException())
        else -> Single.just(file.absolutePath)
    }

    private fun formatParentDirectory(dir: String): String {
        return dir.substring(0, currentDirectory.lastIndexOf("/"))
    }
}