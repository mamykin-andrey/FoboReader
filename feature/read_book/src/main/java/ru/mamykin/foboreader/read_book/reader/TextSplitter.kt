package ru.mamykin.foboreader.read_book.reader

/**
 * Component, capable of splitting long text by pages depending on the screen size and other text parameters
 */
interface TextSplitter {

    fun splitText(
        text: String,
        screenSize: Pair<Int, Int>,
        fontSize: Int,
    ): List<String>
}