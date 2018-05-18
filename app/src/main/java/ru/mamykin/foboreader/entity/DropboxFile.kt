package ru.mamykin.foboreader.entity

import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.Metadata

class DropboxFile(var file: Metadata) {
    val name: String = file.name
    val pathLower: String = file.pathLower
    val isDirectory: Boolean = file is FolderMetadata
}