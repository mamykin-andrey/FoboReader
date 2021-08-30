package ru.mamykin.foboreader.read_book.domain.helper

import ru.mamykin.foboreader.read_book.domain.entity.BookContent
import java.io.File
import javax.inject.Inject
import javax.xml.parsers.SAXParserFactory
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BookContentParser @Inject constructor() {

    suspend fun parse(filePath: String): BookContent = suspendCoroutine { cont ->
        runCatching {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val parseHandler = BookContentParserHandler {
                cont.resumeWith(Result.success(it))
            }
            parser.parse(File(filePath), parseHandler)
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWithException(it)
        }
    }
}