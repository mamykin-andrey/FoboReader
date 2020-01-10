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
import ru.mamykin.store.R
import java.io.File


class FileDownloader(
        private val context: Context
) {
    private var downloadCompleteReceiver: DownloadCompleteReceiver? = null
    private val downloadManager: DownloadManager? = context.getSystemService<DownloadManager>()

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
            onSuccess: () -> Unit,
            onError: (Int) -> Unit
    ) {
        downloadManager ?: return
        val downloadsDir = context.externalMediaDir ?: return
        val bookFile = File(downloadsDir, fileName)
        bookFile.takeIf { it.exists() }?.delete()

        val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(title)
                .setDescription(context.getString(descriptionRes))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(bookFile))

        val downloadId = downloadManager.enqueue(request)

        downloadCompleteReceiver = DownloadCompleteReceiver(downloadId, onSuccess, onError)
        context.registerReceiver(
                downloadCompleteReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
    }

    fun release() {
        downloadCompleteReceiver
                ?.let { context.unregisterReceiver(it) }
    }

    private inner class DownloadCompleteReceiver(
            private val downloadId: Long?,
            private val onSuccess: () -> Unit,
            private val onError: (Int) -> Unit
    ) : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val loadedId = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID)
            if (downloadId != null && downloadId == loadedId) {
                val query = DownloadManager.Query().apply { setFilterById(downloadId) }
                val cursor = downloadManager!!.query(query)?.apply { moveToFirst() } ?: return

                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                val reason = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_REASON))
                when {
                    status == DownloadManager.STATUS_SUCCESSFUL -> onSuccess()
                    reason == DownloadManager.ERROR_INSUFFICIENT_SPACE -> onError(R.string.error_insufficient_space)
                    else -> onError(R.string.download_book_common_error)
                }
            }
        }
    }
}