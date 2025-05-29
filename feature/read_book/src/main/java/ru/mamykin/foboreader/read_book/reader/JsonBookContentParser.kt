package ru.mamykin.foboreader.read_book.reader

import com.google.gson.Gson
import ru.mamykin.foboreader.read_book.translation.TextTranslation
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class JsonBookContentParser @Inject constructor() : BookContentParser {

    companion object {
        private const val PRIMARY_LANGUAGE = "nl"
        private const val SECONDARY_LANGUAGE = "en"
    }

    override suspend fun parse(filePath: String): BookContent = suspendCoroutine { cont ->
        runCatching {
            val gson = Gson()
            val parsed = gson.fromJson(File(filePath).reader(), BookContentResponse::class.java)

            // Convert sentences from multilingual format to simple text/translation format
            val sentences = parsed.sentences.map { sentence ->
                val primaryText = sentence.text[PRIMARY_LANGUAGE] ?: ""
                val secondaryText = sentence.text[SECONDARY_LANGUAGE] ?: ""
                TextTranslation(
                    text = primaryText,
                    translation = secondaryText,
                )
            }

            // Flatten the nested dictionary for the selected language pair
            val flattenedDictionary = mutableMapOf<String, String>()
            parsed.dictionary[PRIMARY_LANGUAGE]?.forEach { (word, translations) ->
                translations[SECONDARY_LANGUAGE]?.let { translation ->
                    flattenedDictionary[word] = translation
                }
            }

            cont.resume(
                BookContent(
                    sentences = sentences,
                    dictionary = flattenedDictionary,
                )
            )
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWithException(it)
        }
    }

    private class BookContentResponse(
        val sentences: List<SentenceResponse>,
        val dictionary: Map<String, Map<String, Map<String, String>>>,
    ) {
        class SentenceResponse(
            val text: Map<String, String>,
        )
    }
}