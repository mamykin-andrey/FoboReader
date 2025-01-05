package ru.mamykin.foboreader.my_books.list

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import ru.mamykin.foboreader.common_book_info.domain.model.DownloadedBookEntity
import java.io.File
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal class JsonBookInfoParser @Inject constructor() : BookInfoParser {

    override suspend fun parse(filePath: String): DownloadedBookEntity? = suspendCoroutine { cont ->
        runCatching {
            val gson = Gson()
            val parsed = gson.fromJson(File(filePath).reader(), BookContentResponse::class.java)
            cont.resume(
                DownloadedBookEntity(
                    id = 0,
                    filePath = filePath,
                    genre = parsed.metadata.genre,
                    coverUrl = "",
                    author = parsed.metadata.author.firstName.source + parsed.metadata.author.middleName.source + parsed.metadata.author.lastName.source,
                    title = parsed.metadata.title.source,
                    languages = listOf(),
                    date = Date(),
                    currentPage = 0,
                    lastOpen = System.currentTimeMillis()
                )
            )
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWith(Result.success(null))
        }
    }

    private class BookContentResponse(
        val metadata: Metadata,
        val content: Content,
        val dictionary: Map<String, String>,
    ) {
        class Metadata(
            val genre: String,
            val author: Author,
            val title: TextTranslation,
        )

        class Author(
            @SerializedName("first_name")
            val firstName: TextTranslation,
            @SerializedName("middle_name")
            val middleName: TextTranslation,
            @SerializedName("last_name")
            val lastName: TextTranslation,
        )

        class Content(
            val sentences: List<String>,
            val translations: List<String>,
        )
    }

    private class TextTranslation(
        val source: String,
        val translation: String,
    )
}