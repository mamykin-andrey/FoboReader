package ru.mamykin.foboreader.domain.dropboxbooks

import ru.mamykin.foboreader.data.model.DropboxFile
import ru.mamykin.foboreader.data.repository.DropboxBooksRepository
import rx.Completable
import rx.Single
import javax.inject.Inject

class DropboxBooksInteractor @Inject constructor(
        private val repository: DropboxBooksRepository,
        private var currentDir: String
) {

    fun initDropbox(): Completable {
        return repository.initDropbox()
    }

    fun login(): Completable {
        return repository.loginDropbox()
    }

    fun openDirectory(directory: DropboxFile): Single<List<DropboxFile>> {
        currentDir = directory.pathLower
        return repository.getFiles(currentDir)
    }

    fun openParentDirectory(): Single<List<DropboxFile>> {
        currentDir = formatParentDirectory(currentDir)
        return repository.getFiles(currentDir)
    }

    fun downloadFile(file: DropboxFile): Single<String> {
        return repository.downloadFile(file)
    }

    fun loadAccountInfo(): Single<String> {
        return repository.getAccountInfo()
    }

    fun loadFiles(): Single<List<DropboxFile>> {
        return repository.getFiles(currentDir)
    }

    private fun formatParentDirectory(directoryPath: String): String {
        return directoryPath.substring(0, directoryPath.lastIndexOf("/"))
    }
}