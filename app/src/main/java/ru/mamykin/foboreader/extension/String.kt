package ru.mamykin.foboreader.extension

fun CharSequence.allIndexesOf(char: Char): List<Int> {
    val indices = mutableListOf<Int>()
    this.forEachIndexed { index, currentChar ->
        if (currentChar == char) {
            indices.add(index)
        }
    }
    return indices
}