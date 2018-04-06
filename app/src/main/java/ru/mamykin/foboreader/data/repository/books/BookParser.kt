package ru.mamykin.foboreader.data.repository.books

import ru.mamykin.foboreader.domain.readbook.BookXmlSaxParserHandler
import ru.mamykin.foboreader.entity.FictionBook
import java.io.File
import javax.inject.Inject
import javax.xml.parsers.SAXParserFactory

class BookParser @Inject constructor() {

    fun parse(book: FictionBook, successFunc: () -> Unit) {
        try {
            val factory = SAXParserFactory.newInstance()
            val parser = factory.newSAXParser()
            val parseHandler = BookXmlSaxParserHandler(successFunc, book)
            parser.parse(File(book.filePath), parseHandler)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}