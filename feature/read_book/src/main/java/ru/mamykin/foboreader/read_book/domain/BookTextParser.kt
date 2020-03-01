package ru.mamykin.foboreader.read_book.domain

import java.io.File
import javax.xml.parsers.SAXParserFactory
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BookTextParser {

    suspend fun parse(filePath: String): Pair<List<String>, List<String>> = suspendCoroutine { cont ->
        runCatching {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val parseHandler = BookTextParserHandler { sentences, translations ->
                cont.resumeWith(Result.success(sentences to translations))
            }
            parser.parse(File(filePath), parseHandler)
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWithException(it)
        }
    }
}