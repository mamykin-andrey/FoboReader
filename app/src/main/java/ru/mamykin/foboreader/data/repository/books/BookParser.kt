package ru.mamykin.foboreader.data.repository.books

import ru.mamykin.foboreader.domain.entity.FictionBook
import ru.mamykin.foboreader.domain.readbook.BookXmlSaxParserHandler
import java.io.File
import javax.inject.Inject
import javax.xml.parsers.SAXParserFactory
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BookParser @Inject constructor() {

    suspend fun parse(book: FictionBook) = suspendCoroutine<Unit> { cont ->
        runCatching {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val parseHandler = BookXmlSaxParserHandler({
                cont.resumeWith(Result.success(Unit))
            }, book)
            parser.parse(File(book.filePath), parseHandler)
        }.getOrElse { cont.resumeWithException(it) }
    }
}