package ru.mamykin.foboreader.core.extension

import com.dropbox.core.v2.files.FileMetadata
import ru.mamykin.foboreader.domain.entity.DropboxFile
import java.io.File

// TODO: refactor
val File.isFictionBook: Boolean
    get() = this.name.endsWith(".fb2") || this.name.endsWith(".fbwt")

val DropboxFile.isFictionBook: Boolean
    get() = this.name.endsWith(".fb2") || this.name.endsWith(".fbwt")

val File.attributes: String
    get() {
        val size = this.length()
        val lastModified = this.lastModified()
        return "$size, $lastModified"
    }

val DropboxFile.attributes: String
    get() {
        val fileData = file as FileMetadata
        val size = fileData.size.getBytesSizeString()
        val lastModified = fileData.serverModified.time.getLastModifiedString()
        return "$size, $lastModified"
    }

fun File.getWeight() = when {
    this.isDirectory -> 0
    this.isFictionBook -> 1
    else -> 2
}