package ru.mamykin.foboreader.read_book.domain

import org.xml.sax.Attributes
import org.xml.sax.SAXException
import org.xml.sax.ext.DefaultHandler2

class BookTextParserHandler(
        private val successFunc: (List<String>, List<String>) -> Unit
) : DefaultHandler2() {

    private var currentElement: ElementType = ElementType.Unknown
    private var currentTag: String = ""
    private val sentences = mutableListOf<String>()
    private val translations = mutableListOf<String>()

    override fun startElement(
            uri: String,
            localName: String,
            elemName: String,
            attributes: Attributes
    ) {
        super.startElement(uri, localName, elemName, attributes)
        currentTag = elemName
        currentElement = ElementType.parse(elemName)
    }

    override fun characters(ch: CharArray, start: Int, length: Int) {
        super.characters(ch, start, length)
        val str = String(ch, start, length)
        when (currentElement) {
            ElementType.Paragraph -> sentences.add("$currentTag${str.trim()}/$currentTag")
            ElementType.Translation -> translations.add(str.trim())
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)
        currentTag = ""
        currentElement = ElementType.Unknown
    }

    @Throws(SAXException::class)
    override fun endDocument() {
        super.endDocument()
        successFunc.invoke(sentences, translations)
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