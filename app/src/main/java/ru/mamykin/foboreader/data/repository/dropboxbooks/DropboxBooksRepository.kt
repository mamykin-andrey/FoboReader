package ru.mamykin.foboreader.data.repository.dropboxbooks

import android.os.Environment
import com.dropbox.core.android.Auth
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import ru.mamykin.foboreader.data.exception.UserNotAuthorizedException
import ru.mamykin.foboreader.entity.DropboxFile
import ru.mamykin.foboreader.entity.mapper.FolderToFilesListMapper
import rx.Single
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DropboxBooksRepository @Inject constructor(
        private val clientFactory: DropboxClientFactory,
        private val storage: DropboxBooksStorage,
        private val mapper: FolderToFilesListMapper
) {
    fun getRootDirectoryFiles(): Single<List<DropboxFile>> {
        val authToken = storage.authToken ?: Auth.getOAuth2Token()
        if (authToken?.isNotBlank() == true) {
            storage.authToken = authToken
            return clientFactory.init(authToken).andThen(getFiles(""))
        }
        return Single.error(UserNotAuthorizedException())
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

    fun getAccountEmail(): Single<String> {
        val account = getClient().users().currentAccount
        return Single.just(account.email)
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