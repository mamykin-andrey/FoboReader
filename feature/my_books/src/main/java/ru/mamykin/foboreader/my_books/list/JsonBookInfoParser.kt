package ru.mamykin.foboreader.my_books.list

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBook
import java.io.File
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class JsonBookInfoParser @Inject constructor() : BookInfoParser {

    private val gson = Gson()

    override suspend fun parse(filePath: String): DownloadedBook? = suspendCoroutine { cont ->
        runCatching {
            val parsed = gson.fromJson(File(filePath).reader(), BookInfoResponse::class.java)
            cont.resume(
                DownloadedBook(
                    id = 0,
                    filePath = filePath,
                    genre = parsed.metadata.genre,
                    coverUrl = "",
                    author = parsed.metadata.author,
                    title = parsed.metadata.title,
                    languages = listOf(),
                    date = Date(),
                    currentPage = 0,
                    lastOpen = System.currentTimeMillis(),
                    link = "",
                    rating = 0f,
                    isRatedByUser = false,
                )
            )
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWith(Result.success(null))
        }
    }

    private class BookInfoResponse(
        val metadata: Metadata,
    ) {
        class Metadata(
            val genre: String,
            val author: String,
            val title: String,
        )
    }
}