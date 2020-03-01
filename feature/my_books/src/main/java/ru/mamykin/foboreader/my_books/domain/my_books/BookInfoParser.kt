package ru.mamykin.foboreader.my_books.domain.my_books

import ru.mamykin.foboreader.core.domain.model.BookInfo
import java.io.File
import javax.xml.parsers.SAXParserFactory
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BookInfoParser {

    suspend fun parse(filePath: String): BookInfo = suspendCoroutine { cont ->
        runCatching {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val parseHandler = BookInfoParserHandler(filePath) { bookInfo ->
                cont.resumeWith(Result.success(bookInfo))
            }
            parser.parse(File(filePath), parseHandler)
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWithException(it)
        }
    }
}