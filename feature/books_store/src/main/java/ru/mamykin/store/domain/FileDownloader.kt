package ru.mamykin.store.domain

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import ru.mamykin.core.extension.getExternalMediaDir
import ru.mamykin.core.platform.Log
import java.io.File

class FileDownloader(
        private val context: Context,
        private val client: OkHttpClient
) {
    /**
     * Downloading file by it's [url] into folder media/app_name/[fileName]
     */
    suspend fun download(url: String, fileName: String) = withContext(Dispatchers.IO) {
        val downloadsDir = context.getExternalMediaDir()
                ?: throw DownloadFileException()

        val bookFile = File(downloadsDir, fileName).also {
            it.takeIf { it.exists() }?.delete()
        }

        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
                .takeIf { it.isSuccessful }
                ?: throw DownloadFileException()

        bookFile.sink().buffer().use {
            it.writeAll(response.body!!.source())
        }
        Log.error("File downloaded to ${bookFile.absolutePath}")
    }
}