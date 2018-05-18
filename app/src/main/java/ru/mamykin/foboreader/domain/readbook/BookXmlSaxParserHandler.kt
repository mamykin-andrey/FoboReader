package ru.mamykin.foboreader.domain.readbook

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.ext.DefaultHandler2
import ru.mamykin.foboreader.entity.FictionBook
import ru.mamykin.foboreader.extension.parseDate

// TODO: REFACTOR
class BookXmlSaxParserHandler(
        private val successFunc: () -> Unit,
        private val book: FictionBook
) : DefaultHandler2() {

    private val titleSb = StringBuilder()
    private val textSb = StringBuilder()
    private val transMap = TextHashMap()
    private var lastSentence: String? = null
    private var titleInfo: Boolean = false
    private var inSection: Boolean = false
    private var inTitle: Boolean = false
    private var element: String? = null

    override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
        super.startElement(uri, localName, qName, attributes)

        element = qName
        when (element) {
            "title-info" -> titleInfo = true
            "title" -> inTitle = true
            "section" -> inSection = true
        }
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        super.characters(ch, start, length)

        if (inTitle) {
            // Собираем заголовок
            titleSb.append("<").append(element).append(">")
                    .append(ch, start, length)
                    .append("</").append(element).append(">")
        } else if (inSection) {
            if (element == "p") {
                // Собираем текст
                lastSentence = String(ch, start, length)
                textSb.append("<p>").append(lastSentence).append("</p>")
            } else if (element == "t") {
                // Собираем перевод
                transMap[lastSentence!!.trim()] = String(ch, start, length).trim { it <= ' ' }
            }
        } else
            when (element) {
                "genre" -> book.bookGenre = String(ch, start, length)
                "first-name" -> if (titleInfo)
                    book.bookAuthor = String(ch, start, length) + " "
                else
                    book.docAuthor = String(ch, start, length) + " "
                "last-name" -> if (titleInfo)
                    book.bookAuthor = book.bookAuthor + String(ch, start, length) + " "
                else
                    book.docAuthor = book.docAuthor + String(ch, start, length) + " "
                "middle-name" -> if (titleInfo)
                    book.bookAuthor = book.bookAuthor + String(ch, start, length)
                else
                    book.docAuthor = book.docAuthor + String(ch, start, length)
                "book-title" -> book.bookTitle = String(ch, start, length)
                "lang" -> book.bookLang = String(ch, start, length)
                "src-lang" -> book.bookSrcLang = String(ch, start, length)
                "library" -> book.docLibrary = String(ch, start, length)
                "url" -> book.docUrl = String(ch, start, length)
                "date" -> book.docDate = String(ch, start, length).parseDate()
                "version" -> book.docVersion = java.lang.Double.parseDouble(String(ch, start, length))
            }
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String, localName: String, qName: String) {
        super.endElement(uri, localName, qName)

        element = ""
        when (qName) {
            "title-info" -> titleInfo = false
            "title" -> inTitle = false
            "section" -> inSection = false
        }
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        super.endDocument()

        book.bookText = textSb.toString()
        book.transMap = transMap
        book.sectionTitle = titleSb.toString()
        successFunc()
    }
}