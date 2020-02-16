package ru.mamykin.foboreader.my_books.domain.my_books

import ru.mamykin.foboreader.my_books.domain.model.BookInfo
import java.io.File
import javax.xml.parsers.SAXParserFactory
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BookParser {

    suspend fun parse(filePath: String): BookInfo = suspendCoroutine { cont ->
        runCatching {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val parseHandler = BookXmlSaxParserHandler(filePath) { bookInfo ->
                cont.resumeWith(Result.success(bookInfo))
            }
            parser.parse(File(filePath), parseHandler)
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWithException(it)
        }
    }
}