package ru.mamykin.widget.paginatedtextview.extension

fun CharSequence.allWordPositions(): List<Int> {
    fun isWordDelimiter(char: Char) = char == ' ' || char == '\n'
    val indexes = mutableListOf<Int>()
    for (i in this.indices) {
        if (isWordDelimiter(this[i])) indexes.add(i)
    }
    return indexes
}