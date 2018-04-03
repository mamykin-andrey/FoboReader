package ru.mamykin.foboreader.extension

import ru.mamykin.foboreader.entity.AndroidFile

fun AndroidFile.getWeight() = when {
    this.isDirectory -> 0
    this.isFictionBook -> 1
    else -> 2
}