package ru.mamykin.foboreader.core.extension

import java.io.File

val File.isFictionBook: Boolean
    get() = extension == "fb2" || extension == "fbwt"