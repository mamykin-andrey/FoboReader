package ru.mamykin.foboreader.read_book.reader

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.ext.DefaultHandler2
import ru.mamykin.foboreader.core.platform.Log

class BookContentParserHandler(
    private val successFunc: (TranslatedText) -> Unit
) : DefaultHandler2() {

    companion object {
        private const val TAG = "ContentParserHandler"
    }

    private val sentences = mutableListOf<String>()
    private val lastSentence = StringBuilder()

    private val translations = mutableListOf<String>()
    private val lastTranslation = StringBuilder()

    private var elementType: ElementType = ElementType.Unknown

    override fun startElement(uri: String, localName: String, elemName: String, attributes: Attributes) {
        elementType = ElementType.parse(elemName)
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        val elementStr = String(ch, start, length).trim()
        when (elementType) {
            ElementType.Paragraph -> {
                lastSentence.append(elementStr)
            }

            ElementType.Translation -> {
                lastTranslation.append(elementStr)
            }

            else -> {
                Log.warning(TAG, "No element type for the string: $elementStr!")
            }
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        when (elementType) {
            ElementType.Paragraph -> {
                sentences.add(lastSentence.toString())
                lastSentence.clear()
            }

            ElementType.Translation -> {
                translations.add(lastTranslation.toString())
                lastTranslation.clear()
            }

            else -> {}
        }
        elementType = ElementType.Unknown
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        successFunc.invoke(
            TranslatedText(
                sentences = sentences,
                translations = translations,
            )
        )
    }

    sealed class ElementType {
        data object Paragraph : ElementType()
        data object Translation : ElementType()
        data object Unknown : ElementType()

        companion object {

            fun parse(tag: String): ElementType = when (tag) {
                "p" -> Paragraph
                "t" -> Translation
                else -> Unknown
            }
        }
    }
}