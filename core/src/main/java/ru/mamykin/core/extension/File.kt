package ru.mamykin.core.extension

import java.io.File

val File.isFictionBook: Boolean
    get() = extension == "fb2" || extension == "fbwt"

//val File.attributes: String
//    get() {
//        val size = this.length()
//        val lastModified = this.lastModified()
//        return "$size, $lastModified"
//    }