package ru.mamykin.foboreader.read_book.reader

import com.google.gson.Gson
import ru.mamykin.foboreader.read_book.translation.TextTranslation
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
                    sentences = parsed.content.sentences.map {
                        TextTranslation(
                            source = it.source,
                            translation = it.translation,
                        )
                    },
                    dictionary = parsed.dictionary,
                )
            )
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWithException(it)
        }
    }

    private class BookContentResponse(
        val content: Content,
        val dictionary: Map<String, String>,
    ) {
        class Content(
            val sentences: List<TextTranslation>,
        )

        class TextTranslation(
            val source: String,
            val translation: String,
        )
    }
}