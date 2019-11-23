package ru.mamykin.core.data

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.ext.DefaultHandler2
import ru.mamykin.core.extension.toDate
import ru.mamykin.core.data.model.FictionBook
import java.util.*

class BookXmlSaxParserHandler(
        private val successFunc: () -> Unit,
        private val book: FictionBook
) : DefaultHandler2() {

    private val titleSb = StringBuilder()
    private val textSb = StringBuilder()
    private val translationMap = HashMap<String, String>()
    private var lastSentence: String = ""
    private var currentElement: String = ""

    override fun startElement(uri: String, localName: String, elemName: String, attributes: Attributes) {
        super.startElement(uri, localName, elemName, attributes)
        currentElement = elemName
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        super.characters(ch, start, length)

        val str = String(ch, start, length)
        when (ElementType.parse(currentElement)) {
            ElementType.Title -> titleSb.append(wrapWithTag(str, currentElement))
            ElementType.Paragraph -> lastSentence = str.trim()
                    .also { textSb.append(wrapWithTag(it, currentElement)) }
            ElementType.Translation -> translationMap[lastSentence] = str.trim { it <= ' ' }
            ElementType.BookTitle -> book.bookTitle = str
            ElementType.Lang -> book.bookLang = str
            ElementType.SrcLang -> book.bookSrcLang = str
            ElementType.Library -> book.docLibrary = str
            ElementType.Url -> book.docUrl = str
            ElementType.Date -> book.docDate = str.toDate()
            ElementType.Version -> book.docVersion = str.toDoubleOrNull()
            ElementType.Genre -> book.bookGenre = str
            ElementType.FirstName -> book.bookAuthor = "$str "
            ElementType.LastName -> book.bookAuthor = book.bookAuthor + str + " "
            ElementType.MiddleName -> book.bookAuthor = book.bookAuthor + str
        }
    }

    @Throws(SAXException::class)
    override fun endElement(uri: String, localName: String, qName: String) {
        super.endElement(uri, localName, qName)
        currentElement = ""
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        super.endDocument()

        book.bookText = textSb.toString()
        book.transMap = translationMap
        book.sectionTitle = titleSb.toString()
        successFunc.invoke()
    }

    private fun wrapWithTag(element: String, tag: String): String = "<$tag>$element</$tag>"

    sealed class ElementType(
            val tag: String?
    ) {
        object TitleInfo : ElementType("title-info")
        object Title : ElementType("title")
        object Paragraph : ElementType("p")
        object Translation : ElementType("t")
        object BookTitle : ElementType("book-title")
        object Lang : ElementType("lang")
        object SrcLang : ElementType("src-lang")
        object Library : ElementType("library")
        object Url : ElementType("url")
        object Date : ElementType("date")
        object Version : ElementType("version")
        object Genre : ElementType("genre")
        object FirstName : ElementType("first-name")
        object LastName : ElementType("last-name")
        object MiddleName : ElementType("middle-name")
        object Other : ElementType(null)

        companion object {

            private val types = listOf(
                    TitleInfo,
                    Title,
                    Paragraph,
                    Translation,
                    BookTitle,
                    Lang,
                    SrcLang,
                    Library,
                    Url,
                    Date,
                    Version,
                    Genre,
                    FirstName,
                    LastName,
                    MiddleName,
                    Other
            )

            fun parse(tag: String): ElementType = types.find { it.tag == tag } ?: Other
        }
    }
}