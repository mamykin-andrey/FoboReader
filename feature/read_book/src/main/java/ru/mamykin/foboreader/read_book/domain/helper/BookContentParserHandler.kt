package ru.mamykin.foboreader.read_book.domain.helper

import android.util.Log
import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.ext.DefaultHandler2
import ru.mamykin.foboreader.read_book.domain.model.BookContent

class BookContentParserHandler(
    private val successFunc: (BookContent) -> Unit
) : DefaultHandler2() {

    companion object {
        private const val TAG = "ContentParserHandler"
    }

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
                paragraphs.add(lastSentence)
                translations[lastSentence] = elementStr
                lastSentence = ""
            }

            else -> {
                Log.w(TAG, "No element type for the string: $elementStr!")
            }
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        elementType = ElementType.Unknown
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        successFunc.invoke(
            BookContent(
                paragraphs.joinToString("\n").trim(),
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