package ru.mamykin.foboreader.domain.dropboxbooks

import ru.mamykin.foboreader.data.repository.dropboxbooks.DropboxBooksRepository
import ru.mamykin.foboreader.entity.DropboxFile
import rx.Single
import javax.inject.Inject

class DropboxBooksInteractor @Inject constructor(
        private val repository: DropboxBooksRepository
) {
    private var currentDir: String = ""

    fun getRootDirectoryFiles(): Single<List<DropboxFile>> {
        return repository.getRootDirectoryFiles()
    }

    fun getParentDirectoryFiles(): Single<List<DropboxFile>> {
        currentDir = formatParentDirectory(currentDir)
        return repository.getFiles(currentDir)
    }

    fun getDirectoryFiles(directory: DropboxFile): Single<List<DropboxFile>> {
        currentDir = directory.pathLower
        return repository.getFiles(currentDir)
    }

    fun getAccountEmail(): Single<String> {
        return repository.getAccountEmail()
    }

    fun downloadFile(file: DropboxFile): Single<String> {
        return repository.downloadFile(file)
    }

    private fun formatParentDirectory(directoryPath: String): String {
        return directoryPath.substring(0, directoryPath.lastIndexOf("/"))
    }
}