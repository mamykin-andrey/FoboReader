package ru.mamykin.foboreader.domain.entity

import com.dropbox.core.v2.files.FolderMetadata
import com.dropbox.core.v2.files.Metadata

class DropboxFile(val dropboxFile: Metadata) {
    val name: String = dropboxFile.name
    val pathLower: String = dropboxFile.pathLower
    val isDirectory: Boolean = dropboxFile is FolderMetadata
}