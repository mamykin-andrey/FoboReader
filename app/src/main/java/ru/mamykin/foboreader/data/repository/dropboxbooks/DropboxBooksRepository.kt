package ru.mamykin.foboreader.data.repository.dropboxbooks

import android.os.Environment
import com.dropbox.core.DbxRequestConfig
import com.dropbox.core.android.Auth
import com.dropbox.core.http.OkHttp3Requestor
import com.dropbox.core.v2.DbxClientV2
import com.dropbox.core.v2.files.FileMetadata
import ru.mamykin.foboreader.domain.entity.DropboxFile
import ru.mamykin.foboreader.domain.entity.mapper.FolderToFilesListMapper
import rx.Single
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DropboxBooksRepository @Inject constructor(
        private val storage: DropboxBooksStorage,
        private val mapper: FolderToFilesListMapper
) {
    private val client: DbxClientV2 by lazy {
        val authToken = storage.authToken
                ?: Auth.getOAuth2Token().also { storage.authToken = it }
                ?: throw IllegalStateException("Try to call client method before client initialization!")

        val requestConfig = DbxRequestConfig.newBuilder("FoBo Reader")
                .withHttpRequestor(OkHttp3Requestor.INSTANCE)
                .build()

        DbxClientV2(requestConfig, authToken)
    }

    fun isAuthorized(): Boolean = !storage.authToken.isNullOrBlank()

    fun getFiles(directory: String): Single<List<DropboxFile>> = Single.fromCallable {
        client.files()
                .listFolder(directory)
                .let(mapper::transform)
    }

    fun downloadFile(file: DropboxFile): Single<String> = Single.fromCallable {
        val fileMetadata = file.file as FileMetadata
        val loadedFile = File(getDownloadsDir(), file.name)
        val outputStream = FileOutputStream(loadedFile)

        client.files()
                .download(file.pathLower, fileMetadata.rev)
                .download(outputStream)

        loadedFile.absolutePath
    }

    fun getAccountEmail(): Single<String> = Single.just(
            client.users()
                    .currentAccount
                    .email
    )

    private fun getDownloadsDir(): File {
        val downloadsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        return downloadsDir.takeIf { (it.exists() || it.mkdirs()) && it.isDirectory }
                ?: throw IllegalAccessException("Cannot access Downloads directory!")
    }
}