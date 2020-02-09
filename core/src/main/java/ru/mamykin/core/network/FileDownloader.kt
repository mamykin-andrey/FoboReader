package ru.mamykin.core.network

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import ru.mamykin.core.R
import ru.mamykin.core.extension.getExternalMediaDir
import ru.mamykin.core.platform.Log
import ru.mamykin.core.platform.NotificationUtils
import java.io.File

class FileDownloader(
        private val context: Context,
        private val client: OkHttpClient
) {
    private val notificationManager by lazy { NotificationManagerCompat.from(context) }

    /**
     * Downloading file by it's [url] into folder media/app_name/[fileName]
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
            NotificationEvent.Start -> "Файл загружается"
            NotificationEvent.Finish -> "Загрузка завершена"
            NotificationEvent.Error -> "Не удалось загрузить файл"
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