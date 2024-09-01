package ru.mamykin.foboreader.my_books.list

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.ext.DefaultHandler2
import ru.mamykin.foboreader.common_book_info.domain.model.BookInfo
import ru.mamykin.foboreader.core.extension.toDate
import java.util.*

class BookInfoParserHandler(
    private val filePath: String,
    private val successFunc: (BookInfo) -> Unit
) : DefaultHandler2() {

    companion object {
        private const val TAG = "InfoParserHandler"
    }

    private var currentElement: ElementType = ElementType.Unknown
    private var title = ""
    private var date: Date? = Date()
    private var genre = ""
    private var firstName = ""
    private var lastName = ""
    private var middleName = ""
    private var coverUrl = ""

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
            ElementType.Cover -> coverUrl = str
            else -> {
                Log.w(TAG, "No element type for the string: $str!")
            }
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
                coverUrl = coverUrl,
                author = author,
                title = title,
                languages = listOf(),
                date = date,
                currentPage = 0,
                lastOpen = System.currentTimeMillis()
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
        object Cover : ElementType()
        object Unknown : ElementType()

        companion object {

            fun parse(tag: String): ElementType = when (tag) {
                "book-title" -> BookTitle
                "date" -> Date
                "genre" -> Genre
                "first-name" -> FirstName
                "last-name" -> LastName
                "middle-name" -> MiddleName
                "cover" -> Cover
                else -> Unknown
            }
        }
    }
}