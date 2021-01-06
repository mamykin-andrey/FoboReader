package ru.mamykin.foboreader.read_book.domain.helper

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.ext.DefaultHandler2
import ru.mamykin.foboreader.read_book.domain.entity.BookContent

class BookContentParserHandler(
    private val successFunc: (BookContent) -> Unit
) : DefaultHandler2() {

    private val paragraphs = mutableListOf<String>()
    private val translations = HashMap<String, String>()

    private var elementType: ElementType = ElementType.Unknown
    private var lastSentence: String = ""

    override fun startElement(uri: String, localName: String, elemName: String, attributes: Attributes) {
        elementType = ElementType.parse(elemName)
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        val elementStr = String(ch, start, length).trim()
        when (elementType) {
            ElementType.Paragraph -> {
                lastSentence += elementStr
            }
            ElementType.Translation -> {
                paragraphs.add(lastSentence.asParagraph())
                translations[lastSentence] = elementStr
                lastSentence = ""
            }
        }
    }

    private fun String.asParagraph() = "<p>$this</p>"

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        elementType = ElementType.Unknown
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        successFunc.invoke(
            BookContent(
                paragraphs.joinToString(""),
                translations.takeIf { it.isNotEmpty() }
            )
        )
    }

    sealed class ElementType {
        object Paragraph : ElementType()
        object Translation : ElementType()
        object Unknown : ElementType()

        companion object {

            fun parse(tag: String): ElementType = when (tag) {
                "p" -> Paragraph
                "t" -> Translation
                else -> Unknown
            }
        }
    }
}