package ru.mamykin.foboreader.domain.dropboxbooks

import ru.mamykin.foboreader.data.repository.dropboxbooks.DropboxBooksRepository
import ru.mamykin.foboreader.entity.DropboxFile
import rx.Single
import javax.inject.Inject

class DropboxBooksInteractor @Inject constructor(
        private val repository: DropboxBooksRepository
) {
    private var currentDir: String = ""

    fun getRootDirectoryFiles(): Single<List<DropboxFile>> = repository.getFiles(currentDir)

    // TODO: change to Maybe after migration to RxJava2
    fun getParentDirectoryFiles(): Single<List<DropboxFile>> {
        if (!hasParentDirectory()) {
            return Single.error(RuntimeException("No parent directory"))
        }
        return getParentDirectoryPath().also { currentDir = it }.let { repository.getFiles(it) }
    }

    fun getDirectoryFiles(directory: DropboxFile): Single<List<DropboxFile>> =
            directory.pathLower.also { currentDir = it }.let { repository.getFiles(it) }

    fun getAccountEmail(): Single<String> = repository.getAccountEmail()

    fun downloadFile(file: DropboxFile): Single<String> = repository.downloadFile(file)

    fun isAuthorized(): Boolean = repository.isAuthorized()

    private fun hasParentDirectory(): Boolean = currentDir.lastIndexOf("/") != -1

    private fun getParentDirectoryPath(): String = with(currentDir) { substring(0, lastIndexOf("/")) }
}