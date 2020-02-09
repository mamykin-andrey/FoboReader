package ru.mamykin.my_books.domain.my_books

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.ext.DefaultHandler2
import ru.mamykin.core.extension.toDate
import ru.mamykin.my_books.domain.model.BookInfo
import java.util.*

class BookXmlSaxParserHandler(
        private val filePath: String,
        private val successFunc: (BookInfo) -> Unit
) : DefaultHandler2() {

    private var currentElement: ElementType = ElementType.Unknown
    private var title = ""
    private var date: Date? = Date()
    private var genre = ""
    private var firstName = ""
    private var lastName = ""
    private var middleName = ""

    override fun startElement(
            uri: String,
            localName: String,
            elemName: String,
            attributes: Attributes
    ) {
        super.startElement(uri, localName, elemName, attributes)
        currentElement = ElementType.parse(elemName)
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        super.characters(ch, start, length)
        val str = String(ch, start, length)
        when (currentElement) {
            ElementType.BookTitle -> title = str
            ElementType.Date -> date = str.toDate()
            ElementType.Genre -> genre = str
            ElementType.FirstName -> firstName = str
            ElementType.LastName -> lastName = str
            ElementType.MiddleName -> middleName = str
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)
        currentElement = ElementType.Unknown
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        super.endDocument()
        val author = "$firstName $middleName $lastName"
        successFunc.invoke(
                BookInfo(
                        id = 0,
                        filePath = filePath,
                        genre = genre,
                        coverUrl = null,
                        author = author,
                        title = title,
                        languages = listOf(),
                        date = date,
                        currentPage = 0,
                        lastOpen = System.currentTimeMillis(),
                        format = 0
                )
        )
    }

    sealed class ElementType {
        object BookTitle : ElementType()
        object Date : ElementType()
        object Genre : ElementType()
        object FirstName : ElementType()
        object LastName : ElementType()
        object MiddleName : ElementType()
        object Unknown : ElementType()

        companion object {

            fun parse(tag: String): ElementType = when (tag) {
                "book-title" -> BookTitle
                "date" -> Date
                "genre" -> Genre
                "first-name" -> FirstName
                "last-name" -> LastName
                "middle-name" -> MiddleName
                else -> Unknown
            }
        }
    }
}