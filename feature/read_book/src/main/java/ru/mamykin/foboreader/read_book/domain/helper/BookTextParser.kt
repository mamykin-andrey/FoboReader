package ru.mamykin.foboreader.read_book.domain.helper

import ru.mamykin.foboreader.read_book.domain.model.BookContent
import java.io.File
import javax.xml.parsers.SAXParserFactory
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BookTextParser {

    suspend fun parse(filePath: String): BookContent = suspendCoroutine { cont ->
        runCatching {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val parseHandler = BookTextParserHandler {
                cont.resumeWith(Result.success(it))
            }
            parser.parse(File(filePath), parseHandler)
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWithException(it)
        }
    }
}