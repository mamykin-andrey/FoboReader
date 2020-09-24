package ru.mamykin.foboreader.store.data.network

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import ru.mamykin.foboreader.core.extension.getExternalMediaDir
import ru.mamykin.foboreader.core.platform.Log
import ru.mamykin.foboreader.core.platform.NotificationUtils
import ru.mamykin.foboreader.store.R
import java.io.File

class FileDownloader(
    private val context: Context,
    private val client: OkHttpClient
) {
    private val notificationManager by lazy { NotificationManagerCompat.from(context) }

    /**
     * Download file by its [url] into media/app_name/[fileName] folder
     */
    @Throws(DownloadFileException::class)
    suspend fun download(
        url: String,
        fileName: String,
        showNotification: Boolean = true
    ) {
        val showNotificationFun = if (showNotification) this::showNotification else null

        showNotificationFun?.invoke(fileName, NotificationEvent.Start)

        val newFile = createFile(fileName)
        runCatching { downloadFile(url, newFile) }
            .onSuccess {
                showNotificationFun?.invoke(fileName, NotificationEvent.Finish)
                Log.debug("File downloaded to ${newFile.absolutePath}")
            }
            .onFailure {
                showNotificationFun?.invoke(fileName, NotificationEvent.Error)
                throw it
            }
    }

    private fun createFile(fileName: String): File {
        val downloadsDir = context.getExternalMediaDir()
            ?: throw DownloadFileException()

        return File(downloadsDir, fileName).also {
            it.takeIf { it.exists() }?.delete()
        }
    }

    private suspend fun downloadFile(url: String, bookFile: File) = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()

        val response = client.newCall(request).execute()
            .takeIf { it.isSuccessful }
            ?: throw DownloadFileException()

        bookFile.sink().buffer().use {
            it.writeAll(response.body!!.source())
        }
    }

    private fun showNotification(fileName: String, event: NotificationEvent) {
        val title = when (event) {
            NotificationEvent.Start -> context.getString(R.string.books_store_download_progress)
            NotificationEvent.Finish -> context.getString(R.string.books_store_download_completed)
            NotificationEvent.Error -> context.getString(R.string.books_store_download_failed)
        }
        val notification = NotificationCompat.Builder(context, NotificationUtils.GENERAL_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_download)
            .setContentTitle(title)
            .setContentText(fileName)
            .build()

        notificationManager.notify(NotificationUtils.FILE_DOWNLOAD_NOTIFICATION_ID, notification)
    }

    private sealed class NotificationEvent {
        object Start : NotificationEvent()
        object Finish : NotificationEvent()
        object Error : NotificationEvent()
    }
}