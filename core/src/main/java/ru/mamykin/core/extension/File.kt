package ru.mamykin.core.extension

import java.io.File

// TODO: refactor
val File.isFictionBook: Boolean
    get() = this.name.endsWith(".fb2") || this.name.endsWith(".fbwt")

val File.attributes: String
    get() {
        val size = this.length()
        val lastModified = this.lastModified()
        return "$size, $lastModified"
    }