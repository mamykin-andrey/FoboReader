package ru.mamykin.foboreader.my_books.domain.helper

import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import java.io.File
import javax.inject.Inject
import javax.xml.parsers.SAXParserFactory
import kotlin.coroutines.suspendCoroutine

class BookInfoParser @Inject constructor() {

    suspend fun parse(filePath: String): BookInfo? = suspendCoroutine { cont ->
        runCatching {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val parseHandler = BookInfoParserHandler(filePath) { bookInfo ->
                cont.resumeWith(Result.success(bookInfo))
            }
            parser.parse(File(filePath), parseHandler)
        }.getOrElse {
            it.printStackTrace()
            cont.resumeWith(Result.success(null))
        }
    }
}