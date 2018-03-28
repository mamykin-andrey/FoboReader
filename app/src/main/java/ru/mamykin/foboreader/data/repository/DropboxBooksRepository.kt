package ru.mamykin.foboreader.data.repository

import android.os.Environment
import com.dropbox.core.android.Auth
import com.dropbox.core.v2.files.FileMetadata
import ru.mamykin.foboreader.common.DropboxClientFactory
import ru.mamykin.foboreader.common.FolderToFilesListMapper
import ru.mamykin.foboreader.data.model.DropboxFile
import ru.mamykin.foboreader.data.storage.PreferenceNames
import ru.mamykin.foboreader.data.storage.PreferencesManager
import rx.Completable
import rx.Single
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class DropboxBooksRepository @Inject constructor(
        private val prefManager: PreferencesManager,
        private val mapper: FolderToFilesListMapper
) {
    fun initDropbox(): Completable {
        var authToken = prefManager.getString(PreferenceNames.DROPBOX_TOKEN_PREF, "")
        if (authToken?.isNotBlank() == true) {
            initDropboxClient(authToken)
        }

        val dropboxLogout = prefManager.getBoolean(PreferenceNames.DROPBOX_LOGOUT_PREF)
        authToken = Auth.getOAuth2Token()
        if (!dropboxLogout && authToken?.isNotBlank() == true) {
            initDropboxClient(authToken)
        }

        return Completable.error(IllegalAccessException("Dropbox not authorized"))
    }

    fun loginDropbox(): Completable {
        return Completable.fromCallable {
            prefManager.removeValue(PreferenceNames.DROPBOX_LOGOUT_PREF)
        }
    }

    fun getFiles(directory: String): Single<List<DropboxFile>> {
        val folderListResult = DropboxClientFactory.client.files().listFolder(directory)
        return Single.just(folderListResult).map(mapper::transform)
    }

    fun downloadFile(file: DropboxFile): Single<String> {
        return Single.fromCallable {
            val fileMetadata = file.file as FileMetadata
            val downloadsDir = createDownloadsDir()
            val loadedFile = File(downloadsDir, file.name)

            val outputStream = FileOutputStream(loadedFile)
            DropboxClientFactory.client
                    .files()
                    .download(file.pathLower, fileMetadata.rev)
                    .download(outputStream)
            return@fromCallable loadedFile.absolutePath
        }
    }

    fun getAccountInfo(): Single<String> {
        val account = DropboxClientFactory.client.users().currentAccount
        return Single.just(account.email)
    }

    private fun initDropboxClient(authToken: String) {
        DropboxClientFactory.init(authToken)
    }

    private fun createDownloadsDir(): File {
        val downloadsDir = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        if (!downloadsDir.exists() && !downloadsDir.mkdirs() && downloadsDir.isDirectory) {
            throw IllegalAccessException("Cannot access Downloads directory!")
        }
        return downloadsDir
    }
}