package ru.mamykin.foboreader.store.list

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.buffer
import okio.sink
import java.io.File
import javax.inject.Inject

internal class FileRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val client: OkHttpClient,
) : FileRepository {

    override fun createFile(fileName: String): Result<File> {
        val downloadsDir = context.externalMediaDirs.first()
            ?: return Result.failure(IllegalStateException("No external media dir is found!"))

        return Result.success(
            File(downloadsDir, fileName).also {
                it.takeIf { it.exists() }?.delete()
            }
        )
    }

    override suspend fun downloadFile(url: String, outputFile: File): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            val request = Request.Builder().url(url).build()

            val response = client.newCall(request).execute()
                .takeIf { it.isSuccessful }
                ?: throw RuntimeException("Unable to connect to: $url!")

            outputFile.sink().buffer().use {
                it.writeAll(response.body!!.source())
            }
        }
    }
}