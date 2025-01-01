package ru.mamykin.foboreader.read_book.reader

/**
 * Text measurer that supports checking if a text fits on screen with the set UI parameters - screen size and font size
 */
internal interface TextMeasurer {

    fun isTextFit(
        text: String,
        screenSize: Pair<Int, Int>,
        fontSize: Int,
    ): Boolean
}