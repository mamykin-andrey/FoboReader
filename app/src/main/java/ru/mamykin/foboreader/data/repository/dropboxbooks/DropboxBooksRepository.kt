package ru.mamykin.foboreader.data.repository.dropboxbooks

import android.os.Environment
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import ru.mamykin.foboreader.data.exception.UserNotAuthorizedException
import ru.mamykin.foboreader.entity.DropboxFile
import ru.mamykin.foboreader.entity.mapper.FolderToFilesListMapper
import rx.Completable
import rx.Single
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DropboxBooksRepository @Inject constructor(
        private val clientFactory: DropboxClientFactory,
        private val dropboxBooksStorage: DropboxBooksStorage,
        private val mapper: FolderToFilesListMapper
) {
    fun loginDropbox() {
        clientFactory.getClient().auth()
    }

    fun getRootDirectoryFiles(): Single<List<DropboxFile>> {
        return initDropbox().andThen(getFiles(""))
    }

    fun getFiles(directory: String): Single<List<DropboxFile>> {
        return Single.fromCallable {
            val folder = getClient().files().listFolder(directory)
            return@fromCallable mapper.transform(folder)
        }
    }

    fun downloadFile(file: DropboxFile): Single<String> {
        return Single.fromCallable {
            val fileMetadata = file.file as FileMetadata
            val downloadsDir = createDownloadsDir()
            val loadedFile = File(downloadsDir, file.name)
            val outputStream = FileOutputStream(loadedFile)

            getClient().files()
                    .download(file.pathLower, fileMetadata.rev)
                    .download(outputStream)

            return@fromCallable loadedFile.absolutePath
        }
    }

    fun getAccountInfo(): Single<String> {
        val account = getClient().users().currentAccount
        return Single.just(account.email)
    }

    private fun initDropbox(): Completable {
        val authToken = dropboxBooksStorage.authToken
        if (authToken?.isNotBlank() == true) {
            return Completable.fromAction { clientFactory.init(authToken) }
        } else {
            return Completable.error(UserNotAuthorizedException())
        }
    }

    private fun getClient(): DbxClientV2 = clientFactory.getClient()

    private fun createDownloadsDir(): File {
        val downloadsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (!downloadsDir.exists() && !downloadsDir.mkdirs() && downloadsDir.isDirectory) {
            throw IllegalAccessException("Cannot access Downloads directory!")
        }
        return downloadsDir
    }
}