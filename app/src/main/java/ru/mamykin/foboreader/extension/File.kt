package ru.mamykin.foboreader.extension

import ru.mamykin.foboreader.domain.Utils
import java.io.File

val File.isFictionBook: Boolean
    get() = this.name.endsWith(".fb2") || this.name.endsWith(".fbwt")

val File.attributes: String
    get() = Utils.getSizeString(this.length()) + ", " + Utils.getLastModifiedString(this.lastModified())

fun File.getWeight() = when {
    this.isDirectory -> 0
    this.isFictionBook -> 1
    else -> 2
}