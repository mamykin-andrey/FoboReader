package ru.mamykin.store.domain

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.annotation.StringRes
import androidx.core.content.getSystemService
import ru.mamykin.core.extension.externalMediaDir
import ru.mamykin.core.extension.getLongExtra
import java.io.File

class FileDownloader(
        private val context: Context
) {
    private var downloadCompleteReceiver: DownloadCompleteReceiver? = null

    /**
     * Downloading file by it's [url] into folder media/app_name/[fileName]
     * While downloading displayed notification with title [title] and description [descriptionRes]
     * [callback] will be called after file downloading
     *
     * While freeing resources must to call [release]
     */
    fun download(
            url: String,
            fileName: String,
            title: String,
            @StringRes descriptionRes: Int,
            callback: () -> Unit
    ) {
        val downloadsDir = context.externalMediaDir ?: return
        val bookFile = File(downloadsDir, fileName)

        val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(title)
                .setDescription(context.getString(descriptionRes))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(bookFile))

        val downloadManager: DownloadManager? = context.getSystemService()
        val downloadId = downloadManager?.enqueue(request)

        downloadCompleteReceiver = DownloadCompleteReceiver(downloadId, callback)
        context.registerReceiver(
                downloadCompleteReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    fun release() {
        downloadCompleteReceiver
                ?.let { context.unregisterReceiver(it) }
    }

    private class DownloadCompleteReceiver(
            private val downloadId: Long?,
            private val callback: () -> Unit
    ) : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val loadedId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID)
            if (downloadId == loadedId) {
                callback()
            }
        }
    }
}