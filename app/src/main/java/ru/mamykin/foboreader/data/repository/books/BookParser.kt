package ru.mamykin.foboreader.data.repository.books

import ru.mamykin.foboreader.domain.readbook.BookXmlSaxParserHandler
import ru.mamykin.foboreader.domain.entity.FictionBook
import ru.mamykin.foboreader.core.platform.Log
import java.io.File
import javax.inject.Inject
import javax.xml.parsers.SAXParserFactory

class BookParser @Inject constructor() {

    fun parse(book: FictionBook, successFunc: () -> Unit) {
        try {
            val parser = SAXParserFactory.newInstance().newSAXParser()
            val parseHandler = BookXmlSaxParserHandler(successFunc, book)
            parser.parse(File(book.filePath), parseHandler)
        } catch (e: Exception) {
            Log.error("Ошибка разбора XML структуры книги: " + e.stackTrace.toString())
        }
    }
}