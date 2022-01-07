package ru.mamykin.foboreader.store.data.network

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import ru.mamykin.foboreader.core.di.qualifier.CommonClient
import ru.mamykin.foboreader.core.extension.getExternalMediaDir
import java.io.File
import javax.inject.Inject

internal class FileRepositoryImpl @Inject constructor(
    private val context: Context,
    @CommonClient private val client: OkHttpClient,
) : FileRepository {

    override fun createFile(fileName: String): File {
        val downloadsDir = context.getExternalMediaDir()
            ?: throw DownloadFileException()

        return File(downloadsDir, fileName).also {
            it.takeIf { it.exists() }?.delete()
        }
    }

    override suspend fun downloadFile(url: String, outputFile: File) {
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()

            val response = client.newCall(request).execute()
                .takeIf { it.isSuccessful }
                ?: throw DownloadFileException()

            outputFile.sink().buffer().use {
                it.writeAll(response.body!!.source())
            }
        }
    }
}