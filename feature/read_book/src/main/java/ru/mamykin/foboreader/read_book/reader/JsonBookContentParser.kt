package ru.mamykin.foboreader.read_book.reader

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class JsonBookContentParser @Inject constructor() : BookContentParser {

    override suspend fun parse(filePath: String): BookContent = suspendCoroutine { cont ->
        runCatching {
            val gson = Gson()
            val parsed = gson.fromJson(File(filePath).reader(), BookContentResponse::class.java)
            cont.resume(
                BookContent(
                    sentences = parsed.content.sentences,
                    translations = parsed.content.translations,
                    dictionary = parsed.dictionary,
                )
            )
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWithException(it)
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