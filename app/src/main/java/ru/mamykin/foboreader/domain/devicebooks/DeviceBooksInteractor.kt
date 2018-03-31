package ru.mamykin.foboreader.domain.devicebooks

import ru.mamykin.foboreader.entity.AndroidFile
import ru.mamykin.foboreader.data.repository.DeviceBooksRepository
import rx.Single
import java.util.*
import javax.inject.Inject

class DeviceBooksInteractor @Inject constructor(
        private val repository: DeviceBooksRepository
) {
    private var currentDir: String = ""

    fun getRootDirectoryFiles(): Single<FileStructureEntity> {
        val rootDirectory = repository.getRootDirectory()
        return repository.getFiles(rootDirectory)
                .doOnSuccess(this::sortFiles)
                .zipWith(repository.canReadDirectory(rootDirectory), { files, canRead ->
                    Pair(files, canRead)
                })
                .map { FileStructureEntity(it.first, it.second, currentDir) }
    }

    fun getFiles(currentDir: String): Single<FileStructureEntity> {
        this.currentDir = currentDir

        val parentDirectory = formatParentDirectory(currentDir)
        return repository.getFiles(currentDir)
                .doOnSuccess(this::sortFiles)
                .zipWith(repository.canReadDirectory(parentDirectory), { files, canRead ->
                    Pair(files, canRead)
                })
                .map { FileStructureEntity(it.first, it.second, currentDir) }
    }

    fun openParentDirectory(): Single<FileStructureEntity> {
        return getFiles(formatParentDirectory(currentDir))
    }

    fun openFile(file: AndroidFile): Single<String> {
        if (!file.canRead()) {
            return Single.error(IllegalArgumentException())
        }
        return Single.just(file.absolutePath)
    }

    fun openDirectory(dir: AndroidFile): Single<String> {
        if (!dir.canRead()) {
            return Single.error(IllegalArgumentException())
        }
        return Single.just(dir.absolutePath)
    }

    private fun sortFiles(files: List<AndroidFile>) {
        Collections.sort(files, { f1, f2 -> getFileWeight(f2) - getFileWeight(f1) })
    }

    private fun formatParentDirectory(dir: String): String {
        return dir.substring(0, currentDir.lastIndexOf("/"))
    }

    private fun getFileWeight(file: AndroidFile) = when {
        file.isDirectory -> 2
        file.isFictionBook -> 1
        else -> 0
    }
}